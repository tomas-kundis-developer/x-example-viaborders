package org.viaborders.services;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.viaborders.domain.dijkstra.DijkstraSPA;
import org.viaborders.domain.graph.Graph;
import org.viaborders.domain.graph.GraphFactory;
import org.viaborders.domain.route.Country;
import org.viaborders.domain.route.RouteNotFoundException;
import org.viaborders.utils.CountryFormat;

@Service
@Log4j2
public class CalculateRouteUseCaseService implements CalculateRouteUseCase {

  private final ApplicationContext applicationContext;

  private final CountriesOutputPort countriesOutputPort;

  private final GraphFactory graphFactory;

  /**
   * Constructor.
   */
  @Autowired
  public CalculateRouteUseCaseService(
      ApplicationContext applicationContext,
      CountriesOutputPort countriesOutputPort,
      GraphFactory graphFactory) {
    this.applicationContext = applicationContext;
    this.countriesOutputPort = countriesOutputPort;
    this.graphFactory = graphFactory;
  }

  @Override
  @NotNull
  public List<@NotNull String> calculateRoute(@NotNull String originCountry,
                                              @NotNull String destinationCountry) {
    log.debug("""
              findPath(): ... STARTED
              originCountry: {}
              destinationCountry: {}""", originCountry, destinationCountry);

    // Fetch country list.

    List<Country> countries = countriesOutputPort.fetchAllCountries();

    log.trace("Countries:\n {}", CountryFormat.formattedCountryList(countries));

    // Create graph representation from country list.

    Graph graph = graphFactory.createUndirectedFrom(countries);

    log.debug("Graph:\n{}", graph);

    // Check if the source and target countries exist.
    graph.getNodeOpt(originCountry)
        .orElseThrow(
            () -> new NoSuchElementException("Source country not found: " + originCountry));
    graph.getNodeOpt(destinationCountry)
        .orElseThrow(() -> new NoSuchElementException(
            "Destination country not found: " + destinationCountry));

    return calculateRouteOrThrow(originCountry, destinationCountry, graph);
  }

  private List<String> calculateRouteOrThrow(String originCountry,
                                             String destinationCountry,
                                             Graph graph) {
    // Create new Dijkstra algorithm implementation instance (it's not singleton!).
    DijkstraSPA dijkstra = applicationContext.getBean(DijkstraSPA.class);

    // Associate country's graph representation instance with Dijkstra algorithm.
    dijkstra.setGraph(graph);

    // Run the shortest path algorithm.
    List<String> route = dijkstra.findShortestPath(originCountry, destinationCountry);

    if (route.isEmpty()) {
      throw RouteNotFoundException.builder()
          .origin(originCountry)
          .destination(destinationCountry)
          .includeCountryInMessage(true)
          .exceptionMessage("Route not found!")
          .build();
    }

    return route;
  }
}
