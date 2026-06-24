package org.viaborders.utils;

import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.viaborders.domain.route.Country;

/**
 * Formatted string for {@link Country} and country list.
 */
@UtilityClass
public class CountryFormat {

  /**
   * Format country list.
   */
  public String formattedCountryList(@NonNull List<Country> countries) {
    if (countries.isEmpty()) {
      return "";
    }

    StringBuilder strBuilder = new StringBuilder();
    countries.forEach(country -> {
      strBuilder.append(country);
      strBuilder.append("\n");
    });

    // Remove last new line.
    strBuilder.deleteCharAt(strBuilder.length() - 1);

    return strBuilder.toString();
  }
}
