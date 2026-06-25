package org.viaborders.services;

import java.util.List;
import java.util.NoSuchElementException;
import org.viaborders.domain.route.RouteNotFoundException;

/**
 * Use Case: Calculate land route from origin to destination country.
 */
public interface CalculateRouteUseCase {

  /**
   * Calculate land route from origin to destination country.
   *
   * @return An empty list if route doesn't exist.
   * @throws NoSuchElementException "Source country not found: &lt;originCountry&gt;"
   * @throws NoSuchElementException "Target country not found: &lt;destinationCountry&gt;"
   * @throws RouteNotFoundException Route from origin to destination was not found.
   */
  List<String> calculateRoute(String originCountry, String destinationCountry);
}
