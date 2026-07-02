package org.viaborders.api.rest.v1.routing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.viaborders.api.rest.v1.common.ApiUtils.*;
import static org.viaborders.testdata.TestConstants.*;

import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

/**
 * Integration tests: {@link RoutingController}.
 *
 * <p><b>These tests actually serve also as acceptance tests.</b>
 *
 * <ul>
 * <li>Spring's real embedded server is started on the random HTTP port.</li>
 * <li>Real HTTP requests are made to external systems.</li>
 * </ul>
 *
 * <p><em>Not using {@link org.springframework.test.web.servlet.MockMvc} here.</em>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Log4j2
@SuppressWarnings("MethodName")
class RoutingControllerIT {

  private static final String ROUTING_URL = "/routing/{origin}/{destination}";

  // JSON paths.

  private static final String ERR_RESPONSE_EXCEPTION = "exception";
  private static final String ERR_RESPONSE_MESSAGE = "message";
  private static final String ERR_RESPONSE_PATH = "path";
  private static final String ERR_RESPONSE_STATUS = "status";

  @Autowired
  private RestTestClient restClient;

  @Test
  void testRouting_get_ok_route_to_itself() {

    RoutingResponse httpResponse = restClient
        .get()
        .uri(ROUTING_URL, POLAND, POLAND)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk()
        .expectBody(RoutingResponse.class)
        .returnResult().getResponseBody();

    logHttpResponse("testRouting_get_ok_route_to_itself():", httpResponse);

    assertThat(httpResponse).isNotNull();
    assertThat(httpResponse.hasError()).isFalse();

    assertThat(httpResponse.getOrigin()).isEqualTo(POLAND);
    assertThat(httpResponse.getDestination()).isEqualTo(POLAND);

    assertThat(httpResponse.getRoute())
        .hasSize(1)
        .containsExactly(POLAND);
  }

  /**
   * Calculate short route with only 3 countries.
   *
   * <p>Usable for basic reasonable graph's shortest path search test.
   */

  @Test
  void testRouting_get_ok_short_route() {

    RoutingResponse httpResponse = restClient
        .get()
        .uri(ROUTING_URL, POLAND, AUSTRIA)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk()
        .expectBody(RoutingResponse.class)
        .returnResult().getResponseBody();

    logHttpResponse("testRouting_get_ok_short_route():", httpResponse);

    assertThat(httpResponse).isNotNull();
    assertThat(httpResponse.hasError()).isFalse();

    assertThat(httpResponse.getOrigin()).isEqualTo(POLAND);
    assertThat(httpResponse.getDestination()).isEqualTo(AUSTRIA);

    assertThat(httpResponse.getRoute())
        .hasSize(3)
        // There are more equivalent routes available.
        //
        // Depending on the specific algorithm data structures implementation,
        // or adjacent country order from the external source,
        // if algorithm or external source will change,
        // the test can pass with another countries combination like POL - CZK - AUT.
        .containsExactly(POLAND, SLOVAKIA, AUSTRIA);
  }

  /**
   * Calculate the longest route.
   *
   * <p><em>The longest route was discovered manually by analyzing the world map. Maybe another,
   * real longest route exists, but actually discovered longest route is enought for testing
   * purposes.</em>
   *
   * <ul>
   * <li>From Portugal to Malaysia.</li>
   * <li>Contains 10 nodes (hops) including origin and destination countries.</li>
   * </ul>
   */
  @Test
  void testRouting_get_ok_long_route() {

    RoutingResponse httpResponse = restClient
        .get()
        .uri(ROUTING_URL, PORTUGAL, MALAYSIA)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk()
        .expectBody(RoutingResponse.class)
        .returnResult().getResponseBody();

    logHttpResponse("testRouting_get_ok_long_route():", httpResponse);

    assertThat(httpResponse).isNotNull();
    assertThat(httpResponse.hasError()).isFalse();

    assertThat(httpResponse.getOrigin()).isEqualTo(PORTUGAL);
    assertThat(httpResponse.getDestination()).isEqualTo(MALAYSIA);

    assertThat(httpResponse.getRoute())
        .hasSize(10)
        .containsExactly(
            PORTUGAL, "ESP", "FRA", "DEU", POLAND, "RUS", "CHN", "MMR", "THA", MALAYSIA);
  }

  @Test
  void testRouting_get_not_found_route() {

    RoutingResponse httpResponse = restClient
        .get()
        .uri(ROUTING_URL, SLOVAKIA, GREENLAND)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isBadRequest()
        .expectBody(RoutingResponse.class)
        .returnResult().getResponseBody();

    logHttpResponse("testRouting_get_not_found_route():", httpResponse);

    assertThat(httpResponse).isNotNull();
    assertThat(httpResponse.hasError()).isFalse();

    assertThat(httpResponse.getOrigin()).isEqualTo(SLOVAKIA);
    assertThat(httpResponse.getDestination()).isEqualTo(GREENLAND);

    assertThat(httpResponse.getRoute()).isEmpty();
  }


  // -----------------------------------------------------------------------------------------------
  // Edge test cases
  // -----------------------------------------------------------------------------------------------

  @Test
  void testRouting_get_500_origin_country_not_exist() {
    Map<String, Object> errorResponse = restClient
        .get()
        .uri(ROUTING_URL, NON_EXISTING_COUNTRY, AUSTRIA)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        .expectBody(Map.class)
        .returnResult().getResponseBody();

    assertThat(errorResponse)
        .isNotNull()
        .containsEntry(ERR_RESPONSE_EXCEPTION, NoSuchElementException.class.getName())
        .containsEntry(ERR_RESPONSE_MESSAGE,
            "Source country not found: " + normalizeCountryPathVariable(NON_EXISTING_COUNTRY))
        .containsEntry(ERR_RESPONSE_PATH, "/routing/" + NON_EXISTING_COUNTRY + "/AUT")
        .containsEntry(ERR_RESPONSE_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  @Test
  void testRouting_get_500_destination_country_not_exist() {
    Map<String, Object> errorResponse = restClient
        .get()
        .uri(ROUTING_URL, AUSTRIA, NON_EXISTING_COUNTRY)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        .expectBody(Map.class)
        .returnResult().getResponseBody();

    assertThat(errorResponse)
        .isNotNull()
        .containsEntry(ERR_RESPONSE_EXCEPTION, NoSuchElementException.class.getName())
        .containsEntry(ERR_RESPONSE_MESSAGE,
            "Destination country not found: " + normalizeCountryPathVariable(NON_EXISTING_COUNTRY))
        .containsEntry(ERR_RESPONSE_PATH, "/routing/AUT/" + NON_EXISTING_COUNTRY)
        .containsEntry(ERR_RESPONSE_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  private void logHttpResponse(String prefix, Object httpResponse) {
    log.debug("{} HTTP response:\n{}", prefix, httpResponse);
  }
}