package org.viaborders.api.rest.v1.routing;

import static org.viaborders.api.rest.v1.common.ApiUtils.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.viaborders.domain.route.RouteNotFoundException;
import org.viaborders.services.CalculateRouteUseCase;

/**
 * Calculate land route from origin to destination country.
 */
@RestController
@RequestMapping("/routing")
@Log4j2
public class RoutingController {

  private final CalculateRouteUseCase calculateRouteUseCase;

  /**
   * Constructor.
   */
  public RoutingController(CalculateRouteUseCase calculateRouteUseCase) {
    this.calculateRouteUseCase = calculateRouteUseCase;
  }

  /**
   * Calculated land route from {@code origin } to {@code destination} country.
   *
   * @param origin      The country where the path begins.
   * @param destination The destination country.
   * @return {@link RoutingResponse} The {@code origin} and {@code destination} countries
   * are included in calculated the path.
   */
  @GetMapping(
      path = "/{origin}/{destination}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Valid
  public RoutingResponse routing(@PathVariable @NotBlank String origin,
                                 @PathVariable @NotBlank String destination) {

    log.debug("/routing: GET ...... STARTED");

    String originCountryNormalized = normalizeCountryPathVariable(origin);
    String destinationCountryNormalized = normalizeCountryPathVariable(destination);

    log.debug("/routing: GET: originCountryNormalized=[{}], destinationCountryNormalized=[{}]",
        originCountryNormalized, destinationCountryNormalized);

    List<String> path = calculateRouteUseCase.calculateRoute(
        originCountryNormalized,
        destinationCountryNormalized);

    log.debug("/routing: GET ...... DONE (HTTP 200 OK)");

    return RoutingResponse.builder()
        .origin(origin)
        .destination(destination)
        .route(path)
        .build();
  }

  /**
   * Handle {@link RouteNotFoundException}.
   *
   * <p>Use case: Route is not found (not exist).
   *
   * @see CalculateRouteUseCase#calculateRoute(String, String)
   */
  @ExceptionHandler(RouteNotFoundException.class)
  public ResponseEntity<RoutingResponse> handle(RouteNotFoundException ex) {
    log.debug("/routing: GET ...... DONE (HTTP 400 Bad Request)");

    return ResponseEntity
        .badRequest()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            RoutingResponse.builder()
                .origin(ex.getOrigin())
                .destination(ex.getDestination())
                .route(List.of())
                .build()
        );
  }
}
