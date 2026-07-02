package org.viaborders.api.rest.v1.common;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base, mandatory data for every HTTP response.
 */
@SuperBuilder
@NoArgsConstructor
public abstract class BaseResponse {

  public static final String API_VERSION = "1.0";

  /**
   * Timestamp of the response creation.
   *
   * <p>Created automatically in UTC time zone when instantiating class.
   */
  @JsonProperty
  @NotNull
  private final LocalDateTime timestamp = LocalDateTime.now(ZoneId.from(ZoneOffset.UTC));

  /**
   * Actual API version.
   *
   * <p>Always constant value from {@link #API_VERSION}. No setter for this field.
   */
  @JsonGetter
  @NotNull
  public String getApiVersion() {
    return API_VERSION;
  }

  /**
   * Error response indicator.
   *
   * <p>The {@code true} value indicates that error occurs during the HTTP request processing,
   * and error is returned instead of standard HTTP response entity.
   */
  @JsonGetter
  @NotNull
  public boolean hasError() {
    // TODO: ASAP: Add logic to return true if there will be error description object.
    return false;
  }
}
