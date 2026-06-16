package org.viaborders.domain.dijkstra;

import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.viaborders.domain.graph.Edge;
import org.viaborders.domain.graph.Graph;
import org.viaborders.domain.graph.Node;

/**
 * Dijkstra shortest path algorithm.
 *
 * <p>This implementation works only with undirected graph.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Validated
@Log4j2
public class DijkstraSPA {

  static final int INFINITY = 999_999_999;

  static final int SOURCE_NODE_DISTANCE = 0;

  @Getter(AccessLevel.PACKAGE)
  private final Map<String, String> ancestors = new HashMap<>();

  @Getter(AccessLevel.PACKAGE)
  private final Map<String, Integer> distances = new HashMap<>();

  private final UnsettledNodes unsettledNodes = new UnsettledNodes();

  @Setter
  private Graph graph;

  /**
   * Find any shortest path in the graph from source to target node.
   */
  @NotNull
  public List<String> findShortestPath(@NotNull String sourceNodeId, @NotNull String targetNodeId) {
    log.debug("""
              findShortestPath(): ... STARTED
              sourceNodeId: {}
              targetNodeId: {}""", sourceNodeId, targetNodeId);

    Objects.requireNonNull(graph, "Graph is not set!");

    initializeData(sourceNodeId);

    log.debug("DIJKSTRA SHORT PATH ALGORITHM ...... STARTED");

    searchShortestPath();

    log.debug("DIJKSTRA SHORT PATH ALGORITHM ...... DONE");

    logAlgorithmDataStructures();

    List<String> path = constructShortestPath(sourceNodeId, targetNodeId);

    log.debug("Shortest path:\n{}", path);
    log.debug("findShortestPath() ... DONE");

    return path;
  }

  /**
   * Edge relaxation.
   *
   * <p>Edge relaxation - updating the edge weight with lower value
   * if shorter path to tho adjacent node was found.
   *
   * <p>This is the core and the main logic of Dijkstra shortest path algorithm.
   *
   * @param node         Actually processed node in Dijkstra's algorithm.
   * @param adjacentNode Some adjacent node of actually processed node.
   */
  void relaxEdgeIfNeeded(@NotNull final Node node, @NotNull final Node adjacentNode) {
    log.debug("relaxEdgeIfNeeded() ... STARTED");

    // TODO: 2026-06-16: TOKU: Check, if they are really adjacent nodes.

    String nodeId = node.getId();
    String adjacentNodeId = adjacentNode.getId();

    int nodeDistance = this.distances.get(nodeId);
    int adjacentNodeDistance = this.distances.get(adjacentNodeId);

    log.debug("Distance before relaxation: {}", adjacentNodeDistance);

    if (adjacentNodeDistance > nodeDistance + 1) {
      log.debug("Edge relaxed!");

      // Update the edge weight between node and its adjacent node.
      // Update twice: for both nodes:
      // => because of undirected graph => they are adjacent each to other
      var updatedNodeDistance = nodeDistance + 1;

      log.debug("Distance after relaxation: {}", updatedNodeDistance);

      distances.replace(adjacentNodeId, updatedNodeDistance);
      unsettledNodes.updateNodeDistance(adjacentNodeId, updatedNodeDistance);

      // Set ancestor of adjacent node to the (actually processed) node.
      ancestors.put(adjacentNodeId, nodeId);
    } else {
      log.debug("Edge not relaxed!");
    }

    log.debug("relaxEdgeIfNeeded() ... DONE");
  }

  /**
   * Shortest path constructed from the {@link #ancestors}.
   *
   * @return Shortest path or empty list if the path not exist.
   */
  private List<String> constructShortestPath(@NotNull String sourceNodeId,
                                             @NotNull String targetNodeId) {
    log.debug("constructShortestPath(): ... STARTED");

    List<String> path = new LinkedList<>();
    String nodeId = targetNodeId;

    while (nodeId != null) {
      log.debug("constructShortestPath(): [{}] has ancestor -->", nodeId);
      path.addFirst(nodeId);
      nodeId = ancestors.get(nodeId);
    }
    log.debug("constructShortestPath(): --> no ancestor.");

    if (path.isEmpty()
        || !sourceNodeId.equals(path.getFirst())) {
      log.debug("constructShortestPath(): The first node on the path: [{}] is not source node id!",
          path.getFirst());
      log.debug("constructShortestPath(): Shortest path not exist!");
      path = List.of();
    }

    log.debug("constructShortestPath(): ... DONE");

    return path;
  }

  /**
   * Initialize data structures before algorithm runs.
   *
   * <ul>
   *   <li>Unsettled nodes queue: Add all graph's nodes with initial distance INFINITY. Set start
   *   node distance to 0.</li>
   *   <li>List of node distances (from start node): Add all graph's nodes with initial distance
   *   INFINITY. Set start node distance to 0.</li>
   *   <li>List of node's ancestors: Add all graph's nodes without ancestor: {@code null} value
   *   .</li>
   * </ul>
   */
  private void initializeData(@NotNull String sourceNodeId) {

    unsettledNodes.clear();
    distances.clear();
    ancestors.clear();

    // Set all nodes with associates values in appropriate data structures.
    graph.getNodeIds()
        .forEach(nodeId -> {
          unsettledNodes.addNode(nodeId, INFINITY);
          distances.put(nodeId, INFINITY);
          ancestors.put(nodeId, null);
        });

    // Initialize the source node.
    var distanceToSourceNode = 0;
    unsettledNodes.updateNodeDistance(sourceNodeId, distanceToSourceNode);
    distances.replace(sourceNodeId, distanceToSourceNode);
    ancestors.replace(sourceNodeId, null);

    log.debug("initializeData(): Data structures initialized to initial values:");
    logAlgorithmDataStructures();
  }

  private void logAlgorithmDataStructures() {
    log.debug("""
              Actual algorithm data structures state:
              ancestors:\t\t{}
              distances:\t\t{}
              unsettledNodes:\t{}""", ancestors, distances, unsettledNodes);
  }

  private void searchShortestPath() {
    log.debug("----");
    while (unsettledNodes.isNotEmpty()) {
      String unsettledNodeId = unsettledNodes.removeNodeWithLowestDistance().orElseThrow();

      log.debug("Processing node with lowest distance: [{}]", unsettledNodeId);

      Queue<Edge> edges = graph.getNode(unsettledNodeId).getEdges();

      edges.forEach(edge -> {
        String targetNode = edge.getTargetNodeId();
        log.debug("Edge: [{}] --> [{}] / weight={}",
            unsettledNodeId, targetNode, edge.getWeight());

        relaxEdgeIfNeeded(graph.getNode(unsettledNodeId), graph.getNode(targetNode));
      });

      logAlgorithmDataStructures();
      log.debug("----");
    }
  }
}