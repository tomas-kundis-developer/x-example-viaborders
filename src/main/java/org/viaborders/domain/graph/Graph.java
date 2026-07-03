package org.viaborders.domain.graph;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import lombok.extern.log4j.Log4j2;

/**
 * Undirected graph model.
 *
 * <p>Graph representation based on the hash table ({@link Map}).
 *
 * <p>Model structure:
 *
 * <pre>
 * ┌─┐   ┌─┐   ┌─┐
 * │A├───┤B├───┤D│
 * └┬┘   └─┘   └─┘
 *  │ ┌─┐
 *  └─┤C│
 *    └─┘</pre>
 *
 * <pre>Map = {
 *    A --> [B/1, C/1]
 *    B --> [A/1, D/1]
 *    C --> [A/1]
 *    D --> [B/1]
 *  }</pre>
 *
 * <p>More versatile description close to Java code:
 *
 * <pre>Map = {
 *    A --> {id: A, edges: [{B, 1}, {C, 1}] }
 *    B --> {id: B, edges: [{A, 1}, {D, 1}] }
 *    C --> {id: C, edges: [{A, 1}]         }
 *    D --> {id: D, edges: [{B, 1}]         }
 *  }
 *  </pre>
 *
 * <ul>
 *   <li>Map key: The node's id: {@link Node#getId()}</li>
 *   <li>Map value: The {@link Node}</li>
 *   <li>Edges between two nodes - a list of edges associated with a concrete node:
 *   {@link Node#getEdges()}</li>
 *   <li>Node's adjacent nodes - as a list of edges associated with this concrete node
 *   (see previous statement about edges).</li>
 * </ul>
 *
 * <p><em>For simplicity and straight-forward implementation,
 * the key to the hashtable is id string of the node, not the node object itself.</em>
 *
 * @see Node
 * @see Edge
 */
@Log4j2
public class Graph {

  /**
   * Graph model.
   */
  private final Map<String, Node> graphModel = new HashMap<>();

  /**
   * Constructor (hidden).
   */
  private Graph() {
  }

  /**
   * Add a new node to the graph.
   *
   * @throws IllegalArgumentException If node with the same {@link Node#getId()} already exist.
   */
  @NotNull
  public Graph addNode(@Valid @NotNull Node node) {
    if (graphModel.containsKey(node.getId())) {
      throw new IllegalArgumentException("Node already exists!");
    }

    graphModel.put(node.getId(), node);
    return this;
  }

  /**
   * Add a new edge to the graph.
   *
   * <p>Edges point to the same node (source = target) are allowed.
   *
   * <p>On the graph's data structure implementation level, it'll implicitly add "edge connector"
   * also from the other side - from {@code node2id} to {@code node1id}.
   *
   * <p><b>This implies, that {@link Node} with {@code node2id} must always exist before calling
   * this method</b>.
   *
   * @param node1id Source node (the start of the edge).
   * @param node2id Target node (the end of the node).
   * @throws NoSuchElementException If source or target node of the edge's endpoints doesn't exist.
   */
  @NotNull
  public Graph addUndirectedEdge(@NotNull String node1id, @NotNull String node2id, int weight) {

    log.debug("addUndirectedEdge(): Adding edge: [{}]---({})---[{}]", node1id, weight, node2id);

    // If node1 or node2 doesn't exist --> NoSuchElementException
    log.debug("addUndirectedEdge(): Finding graph's node: node1id: {}", node1id);
    Node node1 = getNodeOpt(node1id).orElseThrow();

    log.debug("addUndirectedEdge(): Finding graph's node: node2id: {}", node2id);
    Node node2 = getNodeOpt(node2id).orElseThrow();

    // Node1: Add adjacent node2.
    // If edge node1 --> node2 always exist then do nothing.
    if (notContainsEdgeToTargetNode(node1, node2id)) {
      node1.addEdge(new Edge(node2id, weight));
    }

    // Node1: Add adjacent node2.
    // If edge node2 --> node1 always exist then do nothing.
    if (notContainsEdgeToTargetNode(node2, node1id)) {
      node2.addEdge(new Edge(node1id, weight));
    }

    return this;
  }

  /**
   * Return node with specified id.
   *
   * @throws IllegalArgumentException Node doesn't exist.
   */
  @NotNull
  public Node getNode(@NotNull String id) {
    Node node = graphModel.get(id);

    if (node == null) {
      throw new IllegalArgumentException("Node does not exists!");
    }

    return node;
  }

  /**
   * Return node with specified id.
   */
  public Optional<Node> getNodeOpt(@NotNull String id) {
    return Optional.ofNullable(graphModel.get(id));
  }

  /**
   * Return count of all nodes in the graph.
   */
  public int getNodeCount() {
    return graphModel.size();
  }

  /**
   * Return immutable copy of all node ids in actual graph.
   *
   * @return Unmodifiable {@link Set}.
   */
  @NotNull
  public Set<String> getNodeIds() {
    return Set.copyOf(graphModel.keySet());
  }

  @Override
  public String toString() {
    return graphModel.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(entry -> entry.getKey() + " --> " + entry.getValue() + "\n")
        .toList()
        .toString();
  }

  /**
   * Create an empty graph without nodes.
   */
  public static Graph empty() {
    return new Graph();
  }

  /**
   * Find out if a source node contains edge to target node.
   *
   * <p>This auxiliary method was created to help the initial one-time graph creation.<br/>
   * Because of the time complexity O(N),<br/>
   * <b>this method is not suitable to be heavily used in graph algorithms.</b>
   *
   * <p>Returns {@code true} if the edge between source and target node exist.<br/>
   * It checks if the edge is only saved in sourceNode.<br/>
   * For full check and validity of existing edge, you must also check edge existing on target node.
   */
  private boolean notContainsEdgeToTargetNode(@NotNull Node sourceNode,
                                              @NotNull String targetNodeId) {
    return sourceNode.getEdges().stream()
        .noneMatch(edge -> edge.getTargetNodeId().equals(targetNodeId));
  }
}
