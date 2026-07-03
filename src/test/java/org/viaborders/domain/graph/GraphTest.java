package org.viaborders.domain.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class GraphTest {

  private static final String NODE_NOT_EXIST_ID = "test-non-eixsting-node-id";

  /**
   * Test to add edge.
   *
   * <p>Source node: NodeA.
   *
   * <p>Target node: NodeB.
   */
  @Test
  void testAddUndirectedEdge() {
    Graph graph = Graph.empty();

    var nodeAid = "test-node-A";
    var nodeBid = "test-node-B";

    Node nodeA = new Node(nodeAid);
    Node nodeB = new Node(nodeBid);

    assertThat(graph.getNodeCount()).isZero();

    graph.addNode(nodeA);
    graph.addNode(nodeB);

    assertThat(graph.getNodeCount()).isEqualTo(2);

    assertThat(graph.getNode(nodeAid).getEdges()).isEmpty();
    assertThat(graph.getNode(nodeBid).getEdges()).isEmpty();

    var edgeWeight = 1;

    graph.addUndirectedEdge(nodeAid, nodeBid, edgeWeight);

    var edgesA = graph.getNode(nodeAid).getEdges();
    var edgesB = graph.getNode(nodeBid).getEdges();

    assertThat(edgesA).hasSize(1);
    assertThat(edgesB).hasSize(1);

    Edge edgeInA = edgesA.poll();
    Edge edgeInB = edgesB.poll();

    assertThat(edgeInA).isNotNull();
    assertThat(edgeInA.getTargetNodeId()).isEqualTo(nodeBid);
    assertThat(edgeInA.getWeight()).isOne();

    assertThat(edgeInB).isNotNull();
    assertThat(edgeInB.getTargetNodeId()).isEqualTo(nodeAid);
    assertThat(edgeInB.getWeight()).isOne();
  }

  /**
   * Test to add edge.
   *
   * <p>Source node: NodeB.
   *
   * <p>Target node: NodeA.
   *
   * <p>Same test like a {@link #testAddUndirectedEdge()}, only source and target are swapped.<br/>
   * In the result, this have the same effect as not-swaped order of method's arguments.
   */
  @Test
  void testAddUndirectedEdge_inOppositeWay() {
    Graph graph = Graph.empty();

    var nodeAid = "test-node-A";
    var nodeBid = "test-node-B";

    Node nodeA = new Node(nodeAid);
    Node nodeB = new Node(nodeBid);

    assertThat(graph.getNodeCount()).isZero();

    graph.addNode(nodeA);
    graph.addNode(nodeB);

    assertThat(graph.getNodeCount()).isEqualTo(2);

    assertThat(graph.getNode(nodeAid).getEdges()).isEmpty();
    assertThat(graph.getNode(nodeBid).getEdges()).isEmpty();

    var edgeWeight = 1;

    graph.addUndirectedEdge(nodeBid, nodeAid, edgeWeight);

    var edgesA = graph.getNode(nodeAid).getEdges();
    var edgesB = graph.getNode(nodeBid).getEdges();

    assertThat(edgesA).hasSize(1);
    assertThat(edgesB).hasSize(1);

    Edge edgeInA = edgesA.poll();
    Edge edgeInB = edgesB.poll();

    assertThat(edgeInA).isNotNull();
    assertThat(edgeInA.getTargetNodeId()).isEqualTo(nodeBid);
    assertThat(edgeInA.getWeight()).isOne();

    assertThat(edgeInB).isNotNull();
    assertThat(edgeInB.getTargetNodeId()).isEqualTo(nodeAid);
    assertThat(edgeInB.getWeight()).isOne();
  }

  @Test
  void testGetNode() {
    var nodeAid = "test-node-A";

    Graph graph = Graph.empty();
    Node nodeA = new Node(nodeAid);

    assertThat(graph.getNodeCount()).isZero();

    graph.addNode(nodeA);

    Node nodeResult = graph.getNode(nodeAid);

    assertThat(graph.getNodeCount()).isEqualTo(1);
    assertThat(nodeResult.getId()).isEqualTo(nodeAid);
    assertThat(nodeResult.getEdges()).isEmpty();
  }

  @Test
  void testGetNode_notExist() {
    Graph graph = Graph.empty();

    assertThat(graph.getNodeCount()).isZero();

    Throwable thrown = catchThrowable(() -> graph.getNode(NODE_NOT_EXIST_ID));

    then(thrown)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContainingAll("Node does not exists!");
  }

  @Test
  void testGetNodeOpt() {
    var nodeAid = "test-node-A";

    Graph graph = Graph.empty();
    Node nodeA = new Node(nodeAid);

    assertThat(graph.getNodeCount()).isZero();

    graph.addNode(nodeA);

    assertThat(graph.getNodeCount()).isEqualTo(1);

    Optional<Node> nodeResultOpt = graph.getNodeOpt(nodeAid);

    assertThat(nodeResultOpt).isPresent();

    Node nodeResult = nodeResultOpt.get();

    assertThat(nodeResult.getId()).isEqualTo(nodeAid);
    assertThat(nodeResult.getEdges()).isEmpty();
  }

  @Test
  void testGetNodeOpt_notExist() {
    Graph graph = Graph.empty();

    assertThat(graph.getNodeCount()).isZero();

    Optional<Node> nodeResultOpt = graph.getNodeOpt(NODE_NOT_EXIST_ID);

    assertThat(nodeResultOpt).isEmpty();
  }

  @Test
  void testGetNodeIds() {
    Graph graph = Graph.empty();

    graph.addNode(new Node("test-node-A"));
    graph.addNode(new Node("test-node-B"));

    assertThat(graph.getNodeIds())
        .hasSize(2)
        .containsOnly("test-node-A", "test-node-B");
  }

  @Test
  void testGetNodeIds_empty() {
    Graph graph = Graph.empty();

    assertThat(graph.getNodeIds())
        .isEmpty();
  }

  @Test
  void testEmpty() {
    Graph graph = Graph.empty();
    assertThat(graph.getNodeCount()).isZero();
  }
}