package org.viaborders.api.rest.v1.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * REST API v1 utils.
 */
@UtilityClass
public class ApiUtils {

  /**
   * Normalize country name from URL path for use in application.
   *
   * <ul>
   *   <li>Delete all whitespaces.</li>
   *   <li>Trim control character from the beginning and the end of the string.</li>
   *   <li>Convert to upper case.</li>
   * </ul>
   */
  public String normalizeCountryPathVariable(@NonNull String country) {
    return StringUtils.deleteWhitespace(
        StringUtils.trim(country)
    ).toUpperCase();
  }
}
