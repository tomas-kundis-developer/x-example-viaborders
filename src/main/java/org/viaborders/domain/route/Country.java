package org.viaborders.domain.route;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.ToString;

/**
 * The Country domain entity.
 *
 * <p>For a brevity, this class is also JSON POJO model of
 * <a href="https://mledoze.github.io/countries/">World countries in JSON</a>.
 *
 * <p>Parts of the Country domain entity:
 * <ul>
 *   <li>{@link #borders}</li>
 *   <li>{@link #cca3}</li>
 *   <li>{@link #officialName}</li>
 * </ul>
 *
 * <p>Part of the Country JSON POJO model:</p>
 * <ul>
 *   <li>{@link #borders}</li>
 *   <li>{@link #cca3}</li>
 *   <li>{@link #processName(Map)}</li>
 * </ul>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

  /**
   * Land borders.
   */
  @NotNull
  @ToString.Include(rank = 2)
  List<String> borders = List.of();

  /**
   * The Country <code>IISO 3166-1 alpha-3</code> code.
   *
   * <p>A strictly unique two-letter code for every country.
   */
  @NotNull
  @ToString.Include(rank = 3)
  String cca3;

  /**
   * The Country official name in English.
   *
   * <p>JSON property <code>name.official</code>.
   */
  @ToString.Include(rank = 1)
  String officialName;

  /**
   * Map <code>name</code> sub-properties.
   *
   * <p><em>JSON mapper functionality.</em>>
   *
   * <p>Map JSON <code>name.official</code> to {@link #officialName}.
   */
  @JsonProperty("name")
  private void processName(Map<String, Object> name) {
    this.officialName = (String) name.get("official");
  }
}
