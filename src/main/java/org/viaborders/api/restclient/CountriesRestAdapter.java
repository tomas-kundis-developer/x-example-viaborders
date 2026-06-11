package org.viaborders.api.restclient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCEPT_CHARSET;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import org.viaborders.domain.route.Country;
import org.viaborders.services.CountriesOutputPort;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Fetch a list of all countries in JSON format from the World countries REST service.
 *
 * <p>Fetch the list at once as one big resource - the JSON string.
 *
 * <p>This implementation is only suitable for retrieving the list of countries from a network
 * if the size of such list is no more than a few megabytes.
 *
 * <p><b>The method call is blocking until all data has been downloaded.</b>
 *
 * <p><em>This implementation don't use the reactive input data stream that is converted from JSON
 * to domain model just-in-time.</em>
 *
 * <p>Issues (always resolved):
 * <ul>
 *   <li>The REST service always return <code>Content-Type: text/plain; charset=utf-8</code>
 *   instead of <code>application/json</code>.<br/>
 *   You need to consider this fact into account as JSON to POJO conversion won't be done
 *   automatically out-of-the-box by Spring but must be done explicitly.
 *   </li>
 * </ul>
 */
@Repository
@Log4j2
public class CountriesRestAdapter implements CountriesOutputPort {

  private final JsonMapper jsonMapper;

  private final RestClient restClient;

  /**
   * Constructor.
   *
   * <p>{@link RestClient.Builder} is a prototype bean.
   */
  @Autowired
  public CountriesRestAdapter(
      RestClient.Builder restClientBuilder,
      @Value("${viaborders.countries-rest-service.url-json}") String url) {

    // Create Spring RestClient instance.
    this.restClient = restClientBuilder
        .baseUrl(url)
        .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE, "text/plain;q=0.5")
        .defaultHeader(ACCEPT_LANGUAGE, "en-US")
        .defaultHeader(ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
        .build();

    // Create JsonMapper instance.
    this.jsonMapper = JsonMapper.builder()
        // Throw exception when encountering unknown property.
        .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build();
  }

  @Override
  public List<@Valid Country> fetchAllCountries() {

    log.debug("Download all countries (JSON) ............ STARTED");

    ResponseEntity<String> httpResponse = downloadCountries();
    logHttpResponse(httpResponse);

    return mapJsonToCountryList(httpResponse.getBody());
  }

  private ResponseEntity<String> downloadCountries() {
    return restClient
        .get()
        .retrieve()
        .toEntity(String.class);
  }

  private void logHttpResponse(@NotNull ResponseEntity<String> httpResponse) {
    log.debug("Download all countries (JSON) ............ SUCCESS");
    log.debug("Download all countries (JSON): Status code: {}", httpResponse.getStatusCode());
    log.debug("""
              HTTP response headers:
              Content-Type: {}""", httpResponse.getHeaders().getContentType());

  }

  private List<Country> mapJsonToCountryList(@NotNull String jsonStr) {
    return List.copyOf(
        jsonMapper.readValue(jsonStr, new TypeReference<>() {
        }));
  }
}
