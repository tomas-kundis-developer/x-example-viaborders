package org.viaborders.domain.graph;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.viaborders.domain.route.Country;

/**
 * Create undirected graph from the {@link Country} model.
 */
@Service
@Log4j2
public class GraphFactory {

  private static final int DEFAULT_EDGE_WEIGHT = 1;

  /**
   * Create undirected graph from the {@link Country} model.
   *
   * @param countries The list can contain every unique {@link Country#getCca3()} only once.
   */
  @Valid
  @NotNull
  public Graph createUndirectedFrom(@NotNull List<@Valid Country> countries) {
    log.debug("createUndirectedFrom(): ... STARTED");

    var graph = Graph.empty();

    log.debug("createUndirectedFrom(): Adding nodes to the graph.");

    // Add all nodes to the graph - without edges, because before adding an edge, also the target
    // node must be created.
    countries.stream()
        .map(Country::getCca3)
        .forEach(nodeId -> graph.addNode(new Node(nodeId)));

    log.debug("createUndirectedFrom(): Adding edges to the graph.");

    // Add all edges to the graph.
    countries.forEach(country -> country.getBorders()
        .forEach(
            border -> graph.addUndirectedEdge(country.getCca3(), border, DEFAULT_EDGE_WEIGHT)));

    log.debug("createUndirectedFrom(): ... DONE");
    return graph;
  }
}
