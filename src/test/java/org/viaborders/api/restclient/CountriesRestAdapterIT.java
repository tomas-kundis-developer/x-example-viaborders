package org.viaborders.api.restclient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.viaborders.domain.route.Country;
import org.viaborders.services.CountriesOutputPort;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
@Log4j2
class CountriesRestAdapterIT {

  @Autowired
  private CountriesOutputPort countriesOutputPort;

  /**
   * Download country list from the web.
   *
   * <p>Full integration test.
   */
  @Test
  void test() {
    List<Country> countries = countriesOutputPort.fetchAllCountries();

    assertThat(countries).isNotEmpty();

    log.debug("Number of countries: {}", countries.size());
  }
}
