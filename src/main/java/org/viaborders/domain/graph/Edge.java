package org.viaborders.domain.graph;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * The graph edge.
 *
 * <p>Undirected edge associated with the source {@link Node}.
 */
@Value
public class Edge {

  /**
   * The target node to which the edge is associated.
   */
  @NotBlank
  String targetNodeId;

  // TODO: 2026-06-16: TOKU: Custom validation: Value negative, or positive, but not zero.
  /**
   * The weight of the edge.
   */
  int weight;
}
