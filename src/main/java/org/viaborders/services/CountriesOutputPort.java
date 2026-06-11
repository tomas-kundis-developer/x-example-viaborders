package org.viaborders.services;

import jakarta.validation.Valid;
import java.util.List;
import org.viaborders.domain.route.Country;

/**
 * Fetch the list of all countries.
 *
 * <p>The particular data source (file, REST service) and data fetching implementation
 * is implemented by corresponding adapter class implementing this interface.
 *
 * <p>Is the responsibility of adapter to convert source data format (like JSON)
 * to the return value format of corresponding interface's methods.
 */
public interface CountriesOutputPort {

  /**
   * Fetch a list of all countries.
   *
   * @return Unmodifiable {@link List}.
   */
  List<@Valid Country> fetchAllCountries();
}
