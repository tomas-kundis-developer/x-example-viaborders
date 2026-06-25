package org.viaborders.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.viaborders.testdata.TestConstants.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.viaborders.domain.route.RouteNotFoundException;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
class CalculateRouteUseCaseServiceIT {

  @Autowired
  CalculateRouteUseCaseService calculateRouteUseCaseService;

  /**
   * Find route from: POL (Poland) to AUT (Austria).
   *
   * <p>There are 3 equals route with the same total weight = 3:
   * <pre>
   *         | CZK |
   *   POL - | DEU | - AUT
   *         | SVK |
   * </pre>
   */
  @Test
  void testCalculateRoute() {

    List<String> route = calculateRouteUseCaseService.calculateRoute(POLAND, AUSTRIA);

    assertThat(route)
        .hasSize(3)
        .containsOnlyOnce(POLAND, AUSTRIA);

    assertThat(route.get(1))
        .containsAnyOf("CZK", "DEU", "SVK");
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
  void testCalculateRoute_long_route() {
    List<String> route = calculateRouteUseCaseService.calculateRoute(PORTUGAL, MALAYSIA);

    assertThat(route)
        .hasSize(10)
        .containsExactly(
            PORTUGAL, "ESP", "FRA", "DEU", POLAND, "RUS", "CHN", "MMR", "THA", MALAYSIA);
  }

  @Test
  void testCalculateRoute_route_not_exist() {

    Throwable throwable = catchThrowable(
        () -> calculateRouteUseCaseService.calculateRoute(SLOVAKIA, GREENLAND));

    then(throwable)
        .isInstanceOf(RouteNotFoundException.class)
        .hasMessageContainingAll(
            "origin: " + SLOVAKIA,
            "destination: " + GREENLAND,
            "Route not found!"
        );
  }
}