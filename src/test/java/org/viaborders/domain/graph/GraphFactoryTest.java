package org.viaborders.domain.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.viaborders.domain.route.Country;

/**
 * {@link GraphFactory} unit tests.
 *
 * <p>Follow this convention - strictly separate JSON/{@code Country}
 * domain model naming from the {@code Graph} glossary.
 *
 * <p>Reason to follow this rule is to distinguish particular objects and to know, what is
 * country/border in the JSON input vs. the graph model.
 *
 * <ul>
 *   <li>Land item: JSON {@code Country} vs. Graph's {@code Node}</li>
 *   <li>Land name: JSON {@code cca3} vs. Graph's {@code node id}</li>
 *   <li>Land borders: JSON {@code borders} vs. Graph's {@code edges} with {@code adjacent nodes}
 *   .</li>
 * </ul>
 */
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
@SuppressWarnings("AbbreviationAsWordInName")
class GraphFactoryTest {

  @Autowired
  GraphFactory graphFactory;

  @Test
  void testCreateUndirectedFrom_empty() {
    Graph graph = graphFactory.createUndirectedFrom(List.of());

    assertThat(graph.getNodeCount()).isZero();
  }

  /**
   * Test case.
   *
   * <pre>
   *   {A, []}
   * </pre>
   */
  @Test
  void testCreateUndirectedFrom_oneCountryNoBorders() {
    var country = new Country();
    country.setCca3("test-country-A");

    assertThat(country.getBorders()).isEmpty();

    Graph graph = graphFactory.createUndirectedFrom(List.of(country));

    assertThat(graph.getNodeCount()).isOne();
    assertThat(graph.getNode("test-country-A").getEdges()).isEmpty();
  }

  /**
   * Test case.
   *
   * <pre>
   *   {A, [A]}
   * </pre>
   */
  @Test
  void testCreateUndirectedFrom_oneCountryNoSelfBorders() {
    var country = new Country();
    country.setCca3("test-country-A");
    country.setBorders(List.of("test-country-A"));

    assertThat(country.getBorders()).hasSize(1);

    Graph graph = graphFactory.createUndirectedFrom(List.of(country));

    assertThat(graph.getNodeCount()).isOne();
    assertThat(graph.getNode("test-country-A").getEdges()).hasSize(1);
  }

  /**
   * Test case.
   *
   * <pre>
   *   {A, [B, C]}
   *   {B, [A]}
   *   {C, [A]}
   * </pre>
   */
  @Test
  void testCreateUndirectedFrom_oneCountryWithBorders() {
    var countryA = new Country();
    countryA.setCca3("test-country-A");
    countryA.setBorders(List.of("test-country-B", "test-country-C"));

    var countryB = new Country();
    countryB.setCca3("test-country-B");
    countryB.setBorders(List.of("test-country-A"));

    var countryC = new Country();
    countryC.setCca3("test-country-C");
    countryC.setBorders(List.of("test-country-A"));

    Graph graph =
        graphFactory.createUndirectedFrom(List.of(countryA, countryB, countryC));

    assertThat(graph.getNodeCount()).isEqualTo(3);

    Collection<Edge> nodeAEdges = graph.getNode("test-country-A").getEdges();

    assertThat(nodeAEdges).hasSize(2);

    // Check countries on countryA borders = check target nodes ids in countryA edges.
    Set<String> countryIds = nodeAEdges.stream()
        .map(Edge::getTargetNodeId)
        .collect(Collectors.toSet());

    assertThat(countryIds).containsAll(
        List.of("test-country-B", "test-country-C"));
  }

  /**
   * Test case.
   *
   * <ul>
   *   <li>{@code test-country-A} has a border with {@code test-country-B}.</li>
   *   <li>{@code test-country-B} is not included in country list.</li>
   * </ul>
   *
   * <pre>
   *   {A, [B]}
   *   {C, []}
   * </pre>
   */
  @Test
  void testCreateUndirectedFrom_missingBorderCountry() {
    var countryA = new Country();
    countryA.setCca3("test-country-A");
    countryA.setBorders(List.of("test-country-B"));

    var countryC = new Country();
    countryC.setCca3("test-country-C");

    Throwable thrown = catchThrowable(
        () -> graphFactory.createUndirectedFrom(List.of(countryA, countryC)));

    then(thrown)
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("No value present");
  }
}