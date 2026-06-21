package org.viaborders.testdata;

import lombok.experimental.UtilityClass;
import org.viaborders.domain.graph.Graph;
import org.viaborders.domain.graph.Node;

/**
 * Factory for unidirectional graphs to test.
 */
@UtilityClass
public class TestGraphs {

  // ===============================================================================================
  // Elementary graphs.
  // ===============================================================================================

  /**
   * Test graph 1.
   *
   * <p>Only one node.<br/>
   * Source node = target node. No edges.
   *
   * <pre>
   * ┌─┐
   * │A│
   * └─┘
   * </pre>
   */
  public Graph createTestGraph1() {
    return Graph.of()
        .addNode(new Node("test-node-A"));
  }

  /**
   * Test graph 2.
   *
   * <p>Only one node.<br/>
   * Source node = target node. One edge pointing to the node itself.
   *
   * <pre>
   *  ┌──┐
   * ┌┴┐ │
   * │A├─┘
   * └─┘
   * </pre>
   */
  public Graph createTestGraph2() {
    return Graph.of()
        .addNode(new Node("test-node-A"))
        .addUndirectedEdge("test-node-A", "test-node-A", 1);
  }

  /**
   * Test graph 3.
   *
   * <ul>
   *   <li>Graph without edges.</li>
   *   <li>Two disconnected nodes - two islands.</li>
   * </ul>
   *
   * <pre>
   * ┌─┐  ┌─┐
   * │A│  │B│
   * └─┘  └─┘
   * </pre>
   */
  public Graph createTestGraph3() {
    return Graph.empty()
        .addNode(new Node("test-node-A"))
        .addNode(new Node("test-node-B"));
  }

  /**
   * Test graph 4.
   *
   * <ul>
   *   <li>Two disconnected nodes - two islands.</li>
   *   <li>Each node has edge only to itself.</li>
   * </ul>>
   *
   * <pre>
   * ┌─┐    ┌─┐
   * │┌┴┐  ┌┴┐│
   * └┤A│  │B├┘
   *  └─┘  └─┘
   * </pre>
   */
  public Graph createTestGraph4() {
    return Graph.empty()
        .addNode(new Node("test-node-A"))
        .addNode(new Node("test-node-B"))
        .addUndirectedEdge("test-node-A", "test-node-A", 1)
        .addUndirectedEdge("test-node-B", "test-node-B", 1);
  }

  /**
   * Test graph 5.
   *
   * <p>Two nodes connected with one edge.
   *
   * <pre>
   * ┌─┐  ┌─┐
   * │A├──┤B│
   * └─┘  └─┘
   * </pre>
   */
  public Graph createTestGraph5() {
    return Graph.empty()
        .addNode(new Node("test-node-A"))
        .addNode(new Node("test-node-B"))
        .addUndirectedEdge("test-node-A", "test-node-B", 1);
  }

  /**
   * Test graph 6.
   *
   * <p>Three nodes connected in line.</p>
   *
   * <pre>
   * ┌─┐   ┌─┐
   * │A│   │C│
   * └┬┘   └┬┘
   *  │ ┌─┐ │
   *  └─┤B├─┘
   *    └─┘
   * </pre>
   */
  public Graph createTestGraph6() {
    return Graph.empty()
        .addNode(new Node("test-node-A"))
        .addNode(new Node("test-node-B"))
        .addNode(new Node("test-node-C"))
        .addUndirectedEdge("test-node-A", "test-node-B", 1)
        .addUndirectedEdge("test-node-B", "test-node-C", 1);
  }

  /**
   * Test graph 7.
   *
   * <p>Cyclic graph.</p>
   *
   * <pre>
   * ┌─┐   ┌─┐
   * │A├───┤C│
   * └┬┘   └┬┘
   *  │ ┌─┐ │
   *  └─┤B├─┘
   *    └─┘
   * </pre>
   */
  public Graph createTestGraph7() {
    return Graph.empty()
        .addNode(new Node("test-node-A"))
        .addNode(new Node("test-node-B"))
        .addNode(new Node("test-node-C"))
        .addUndirectedEdge("test-node-A", "test-node-B", 1)
        .addUndirectedEdge("test-node-B", "test-node-C", 1)
        .addUndirectedEdge("test-node-C", "test-node-A", 1);
  }

  // ===============================================================================================
  // Complex graphs.
  // ===============================================================================================

  /**
   * Test graph 100.
   *
   * <pre>
   * ┌─┐  ┌─┐
   * │A├──┤B│
   * └┬┘  └┬┘
   *  │    │
   * ┌┴┐  ┌┴┐  ┌─┐
   * │C├──┤D├──┤E│
   * └─┘  └─┘  └─┘
   * </pre>
   */
  public Graph createTestGraph100() {
    return Graph.of()
        .addNode(new Node("test-node-A"))
        .addNode(new Node("test-node-B"))
        .addNode(new Node("test-node-C"))
        .addNode(new Node("test-node-D"))
        .addNode(new Node("test-node-E"))
        .addUndirectedEdge("test-node-A", "test-node-B", 1)
        .addUndirectedEdge("test-node-A", "test-node-C", 1)
        .addUndirectedEdge("test-node-B", "test-node-D", 1)
        .addUndirectedEdge("test-node-D", "test-node-C", 1)
        .addUndirectedEdge("test-node-D", "test-node-E", 1);
  }
}


