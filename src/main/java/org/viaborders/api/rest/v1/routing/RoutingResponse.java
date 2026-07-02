package org.viaborders.api.rest.v1.routing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.viaborders.api.rest.v1.common.BaseResponse;

/**
 * Calculated land route from origin to destination country.
 */
@SuperBuilder
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"origin", "destination", "route", "hasError", "apiVersion", "timestamp"})
public class RoutingResponse extends BaseResponse {

  /**
   * Destination country (from the request).
   */
  @JsonProperty
  @NotBlank
  private String destination;

  /**
   * Origin country (from the request).
   */
  @JsonProperty
  @NotBlank
  private String origin;

  /**
   * Calculated land route.
   *
   * <p>Origin and destination countries are included in the list.
   */
  @JsonProperty
  @NotEmpty
  @Builder.Default
  private List<@NotBlank String> route = List.of();
}
