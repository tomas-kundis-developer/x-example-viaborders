package org.viaborders.services;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.viaborders.testdata.TestConstants.*;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.viaborders.domain.route.Country;
import org.viaborders.domain.route.RouteNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Log4j2
class CalculateRouteUseCaseServiceTest {

  @Autowired
  CalculateRouteUseCaseService calculateRouteUseCaseService;

  @MockitoBean
  private CountriesOutputPort countriesOutputPort;

  @Test
  void testCalculateRoute_origin_country_not_exist() {
    given(countriesOutputPort.fetchAllCountries()).willReturn(List.of());

    Throwable throwable = catchThrowable(
        () -> calculateRouteUseCaseService.calculateRoute(NON_EXISTING_COUNTRY, SLOVAKIA));

    then(throwable)
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("Source country not found: " + NON_EXISTING_COUNTRY);
  }

  @Test
  void testCalculateRoute_destination_country_not_exist() {
    var country1 = new Country();
    country1.setCca3(SLOVAKIA);

    given(countriesOutputPort.fetchAllCountries()).willReturn(List.of(country1));

    Throwable throwable = catchThrowable(
        () -> calculateRouteUseCaseService.calculateRoute(SLOVAKIA, NON_EXISTING_COUNTRY));

    then(throwable)
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("Destination country not found: " + NON_EXISTING_COUNTRY);
  }

  @Test
  void testCalculateRoute_route_not_exist() {
    var country1 = new Country();
    var country2 = new Country();

    country1.setCca3(SLOVAKIA);
    country2.setCca3(POLAND);

    given(countriesOutputPort.fetchAllCountries()).willReturn(List.of(country1, country2));

    Throwable throwable = catchThrowable(
        () -> calculateRouteUseCaseService.calculateRoute(SLOVAKIA, POLAND));

    then(throwable)
        .isInstanceOf(RouteNotFoundException.class)
        .hasMessageContainingAll(
            "origin: " + SLOVAKIA,
            "destination: " + POLAND,
            "Route not found!"
        );
  }
}