package org.viaborders.domain.graph;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import lombok.Value;

/**
 * The graph node.
 */
@Value
public class Node {

  /**
   * The node unique identifier.
   */
  @NotBlank
  String id;

  /**
   * The node's associated edges.
   *
   * <p>Stored as sorted heap.
   */
  @NotNull
  Queue<Edge> edges = new PriorityQueue<>(
      Comparator.comparingInt(Edge::getWeight));

  /**
   * Add edge associated with another node.
   */
  public void addEdge(@Valid @NotNull Edge edge) {
    edges.add(edge);
  }
}
