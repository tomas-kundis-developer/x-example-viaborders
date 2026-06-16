package org.viaborders.domain.dijkstra;

import static org.assertj.core.api.Assertions.*;
import static org.viaborders.testdata.TestGraphs.*;
import static org.viaborders.testutils.TestUtils.*;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.viaborders.domain.graph.Graph;

/**
 * {@link DijkstraSPA} test on the graphs input.
 *
 * <p>Test cases names numbers are mapped to the test graphs number.
 *
 * @see org.viaborders.testdata.TestGraphs
 */
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
@Log4j2
class DijkstraSPAGraphsIT {

  @Autowired
  private ApplicationContext applicationContext;

  private DijkstraSPA dijkstra;

  @BeforeEach
  void beforeEach() {
    this.dijkstra = applicationContext.getBean(DijkstraSPA.class);
  }

  // -----------------------------------------------------------------------------------------------
  // findShortestPath
  // -----------------------------------------------------------------------------------------------

  @Test
  void testFindShortestPath_testCase1() {
    Graph graph = createTestGraph1();

    logGraph(graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-A");

    assertThat(path)
        .hasSize(1)
        .contains("test-node-A");
  }

  @Test
  void testFindShortestPath_testCase2() {

    Graph graph = createTestGraph2();

    logGraph(graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-A");

    assertThat(path)
        .hasSize(1)
        .contains("test-node-A");
  }

  @Test
  void testFindShortestPath_testCase3() {

    Graph graph = createTestGraph3();

    logGraph(graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-B");

    assertThat(path).isEmpty();
  }

  @Test
  void testFindShortestPath_testCase4() {

    Graph graph = createTestGraph4();

    logGraph(graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-B");

    assertThat(path).isEmpty();
  }

  @Test
  void testFindShortestPath_testCase5() {

    Graph graph = createTestGraph5();

    logGraph(graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-B");

    assertThat(path)
        .hasSize(2)
        .containsExactly("test-node-A", "test-node-B");
  }

  @Test
  void testFindShortestPath_testCase6() {

    Graph graph = createTestGraph6();

    logGraph(graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-C");

    assertThat(path)
        .hasSize(3)
        .containsExactly("test-node-A", "test-node-B", "test-node-C");
  }

  @Test
  void testFindShortestPath_testCase7() {

    Graph graph = createTestGraph7();

    logGraph(graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-C");

    assertThat(path)
        .hasSize(2)
        .containsExactly("test-node-A", "test-node-C");
  }

  @Test
  void testFindShortestPath_testCase7_sameInstanceCalledRepeatedly() {

    Graph graph = createTestGraph7();

    logGraph(graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-C");

    assertThat(path)
        .hasSize(2)
        .containsExactly("test-node-A", "test-node-C");

    path = dijkstra.findShortestPath("test-node-A", "test-node-C");

    assertThat(path)
        .hasSize(2)
        .containsExactly("test-node-A", "test-node-C");

    path = dijkstra.findShortestPath("test-node-A", "test-node-B");

    assertThat(path)
        .hasSize(2)
        .containsExactly("test-node-A", "test-node-B");

    path = dijkstra.findShortestPath("test-node-B", "test-node-C");

    assertThat(path)
        .hasSize(2)
        .containsExactly("test-node-B", "test-node-C");
  }

  @Test
  void testFindShortestPath_testCase100() {
    Graph graph = createTestGraph100();

    log.debug("Graph to test:\n{}", graph);

    dijkstra.setGraph(graph);
    List<String> path = dijkstra.findShortestPath("test-node-A", "test-node-E");

    assertThat(path)
        .hasSize(4)
        .containsOnlyOnce("test-node-A", "test-node-D", "test-node-E")
        .containsSubsequence("test-node-D", "test-node-E")
        .containsAnyOf("test-node-B", "test-node-C");

    // These nodes must be exactly on their positions.
    assertThat(path.getFirst()).isEqualTo("test-node-A");
    assertThat(path.get(path.size() - 2)).isEqualTo("test-node-D");
    assertThat(path.getLast()).isEqualTo("test-node-E");
  }
}
