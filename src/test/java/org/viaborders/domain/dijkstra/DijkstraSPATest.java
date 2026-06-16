package org.viaborders.domain.dijkstra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import jakarta.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.viaborders.domain.graph.Edge;
import org.viaborders.domain.graph.Node;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Log4j2
@SuppressWarnings({"java:S117", "AbbreviationAsWordInName", "LocalVariableName"})
class DijkstraSPATest {

  @Autowired
  ApplicationContext applicationContext;

  private DijkstraSPA dijkstra;

  @BeforeEach
  void beforeEach() {
    this.dijkstra = applicationContext.getBean(DijkstraSPA.class);
  }

  // -----------------------------------------------------------------------------------------------
  // testRelaxEdgeIfNeeded
  // -----------------------------------------------------------------------------------------------

  @Test
  void testRelaxEdgeIfNeeded_nullArgs() {
    @SuppressWarnings("DataFlowIssue")
    Throwable thrown = catchThrowable(
        () -> dijkstra.relaxEdgeIfNeeded(null, null));

    then(thrown)
        .isInstanceOf(ConstraintViolationException.class)
        .hasMessageContainingAll(
            "relaxEdgeIfNeeded.node: must not be null",
            "relaxEdgeIfNeeded.adjacentNode: must not be null");
  }

  @Test
  void testRelaxEdgeIfNeeded_testCase1() {
    Node nodeA = new Node("A");
    Node nodeB = new Node("B");

    int edge_A_B_weight = 1;
    nodeA.addEdge(new Edge("B", edge_A_B_weight));
    nodeB.addEdge(new Edge("A", edge_A_B_weight));

    int nodeB_weight_BeforeRelaxation = DijkstraSPA.INFINITY;

    Map<String, Integer> distances = dijkstra.getDistances();
    distances.put("A", DijkstraSPA.SOURCE_NODE_DISTANCE);
    distances.put("B", nodeB_weight_BeforeRelaxation);

    Map<String, String> ancestors = dijkstra.getAncestors();
    ancestors.put("A", null);

    dijkstra.relaxEdgeIfNeeded(nodeA, nodeB);

    int nodeB_weight_expectAfterRelaxation = 1;

    assertThat(distances)
        .containsEntry("A", DijkstraSPA.SOURCE_NODE_DISTANCE)
        .containsEntry("B", nodeB_weight_expectAfterRelaxation);

    assertThat(dijkstra.getAncestors())
        .containsEntry("A", null)
        .containsEntry("B", "A")
        .hasSize(2);
  }

  @Test
  void testRelaxEdgeIfNeeded_testCase2() {
    Node nodeA = new Node("A");
    Node nodeB = new Node("B");

    int edge_A_B_weight = 1;
    nodeA.addEdge(new Edge("B", edge_A_B_weight));
    nodeB.addEdge(new Edge("A", edge_A_B_weight));

    int nodeB_weight_BeforeRelaxation = 1;

    Map<String, Integer> distances = dijkstra.getDistances();
    distances.put("A", DijkstraSPA.SOURCE_NODE_DISTANCE);
    distances.put("B", nodeB_weight_BeforeRelaxation);

    Map<String, String> ancestors = dijkstra.getAncestors();
    ancestors.put("A", null);
    ancestors.put("B", "A");

    dijkstra.relaxEdgeIfNeeded(nodeA, nodeB);

    assertThat(distances)
        .containsEntry("A", DijkstraSPA.SOURCE_NODE_DISTANCE)
        // after relaxation = before relaxation
        .containsEntry("B", nodeB_weight_BeforeRelaxation);

    assertThat(dijkstra.getAncestors())
        .containsEntry("A", null)
        .containsEntry("B", "A")
        .hasSize(2);
  }

  @Test
  void testRelaxEdgeIfNeeded_testCase3() {
    Node nodeA = new Node("A");
    Node nodeB = new Node("B");

    int edge_A_B_weight = 1;
    nodeA.addEdge(new Edge("B", edge_A_B_weight));
    nodeB.addEdge(new Edge("A", edge_A_B_weight));

    int nodeB_weight_BeforeRelaxation = 1;

    Map<String, Integer> distances = dijkstra.getDistances();
    distances.put("A", DijkstraSPA.SOURCE_NODE_DISTANCE);
    distances.put("B", nodeB_weight_BeforeRelaxation);

    Map<String, String> ancestors = dijkstra.getAncestors();
    ancestors.put("A", null);
    ancestors.put("B", "X");

    dijkstra.relaxEdgeIfNeeded(nodeA, nodeB);

    assertThat(distances)
        .containsEntry("A", DijkstraSPA.SOURCE_NODE_DISTANCE)
        // after relaxation = before relaxation
        .containsEntry("B", nodeB_weight_BeforeRelaxation);

    assertThat(dijkstra.getAncestors())
        .containsEntry("A", null)
        .containsEntry("B", "X")
        .hasSize(2);
  }

  @Test
  void testRelaxEdgeIfNeeded_testCase4() {
    Node nodeA = new Node("A");
    Node nodeB = new Node("B");

    int edge_A_B_weight = 1;
    nodeA.addEdge(new Edge("B", edge_A_B_weight));
    nodeB.addEdge(new Edge("A", edge_A_B_weight));

    int nodeB_weight_BeforeRelaxation = 2;

    Map<String, Integer> distances = dijkstra.getDistances();
    distances.put("A", DijkstraSPA.SOURCE_NODE_DISTANCE);
    distances.put("B", nodeB_weight_BeforeRelaxation);

    Map<String, String> ancestors = dijkstra.getAncestors();
    ancestors.put("A", null);
    ancestors.put("B", "A");

    dijkstra.relaxEdgeIfNeeded(nodeA, nodeB);

    int nodeB_weight_expectAfterRelaxation = 1;

    assertThat(distances)
        .containsEntry("A", DijkstraSPA.SOURCE_NODE_DISTANCE)
        .containsEntry("B", nodeB_weight_expectAfterRelaxation);

    assertThat(dijkstra.getAncestors())
        .containsEntry("A", null)
        .containsEntry("B", "A")
        .hasSize(2);
  }

  @Test
  void testRelaxEdgeIfNeeded_testCase5() {
    Node nodeA = new Node("A");
    Node nodeB = new Node("B");

    int edge_A_B_weight = 1;
    nodeA.addEdge(new Edge("B", edge_A_B_weight));
    nodeB.addEdge(new Edge("A", edge_A_B_weight));

    int nodeB_weight_BeforeRelaxation = 2;

    Map<String, Integer> distances = dijkstra.getDistances();
    distances.put("A", DijkstraSPA.SOURCE_NODE_DISTANCE);
    distances.put("B", nodeB_weight_BeforeRelaxation);

    Map<String, String> ancestors = dijkstra.getAncestors();
    ancestors.put("A", null);
    ancestors.put("B", "X");

    dijkstra.relaxEdgeIfNeeded(nodeA, nodeB);

    int nodeB_weight_expectAfterRelaxation = 1;

    assertThat(distances)
        .containsEntry("A", DijkstraSPA.SOURCE_NODE_DISTANCE)
        .containsEntry("B", nodeB_weight_expectAfterRelaxation);

    assertThat(dijkstra.getAncestors())
        .containsEntry("A", null)
        .containsEntry("B", "A")
        .hasSize(2);
  }

  // -----------------------------------------------------------------------------------------------
  // Other auxiliary tests.
  // -----------------------------------------------------------------------------------------------

  @Test
  void testSpringPrototypeBeanScope() {
    DijkstraSPA dijkstraService1 = applicationContext.getBean(DijkstraSPA.class);
    DijkstraSPA dijkstraService2 = applicationContext.getBean(DijkstraSPA.class);

    assertThat(dijkstraService1).isNotSameAs(dijkstraService2);
  }

  @Test
  void testPriorityQueue_sortedByEdgeWeight() {
    Queue<Edge> q = new PriorityQueue<>(
        Comparator.comparingInt(Edge::getWeight));

    q.add(new Edge("Node-A", 8));
    q.add(new Edge("Node-B", 4));
    q.add(new Edge("Node-C", 5));
    q.add(new Edge("Node-E", 4));
    q.add(new Edge("Node-E", 1));

    assertThat(Objects.requireNonNull(q.poll()).getWeight()).isEqualTo(1);
    assertThat(Objects.requireNonNull(q.poll()).getWeight()).isEqualTo(4);
    assertThat(Objects.requireNonNull(q.poll()).getWeight()).isEqualTo(4);
    assertThat(Objects.requireNonNull(q.poll()).getWeight()).isEqualTo(5);
    assertThat(Objects.requireNonNull(q.poll()).getWeight()).isEqualTo(8);
    assertThat(q.poll()).isNull();
  }

  // -----------------------------------------------------------------------------------------------
  // Research.
  // -----------------------------------------------------------------------------------------------

  @Value
  class MyEntry {
    String id;
    int val;
  }

  @Test
  void testMyEntry() {

    var myEntryA1 = new MyEntry("A", 0);
    var myEntryA2 = new MyEntry("A", 0);

    assertThat(Objects.equals(myEntryA1, myEntryA2))
        .isTrue();
    assertThat(Objects.equals(myEntryA1, new MyEntry("A", 0)))
        .isTrue();

    var myEntryC = new MyEntry("C", 2);

    assertThat(Objects.equals(myEntryA1, myEntryC))
        .isFalse();
  }

  @Test
  void testMap_sortKVEntries() {
    Map<String, Integer> m = new HashMap<>();

    m.put("A", 3);
    m.put("B", 1);
    m.put("C", 2);

    assertThat(m.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .toList())
        .containsExactly("B", "C", "A");
  }

  @Test
  void testSortedSet_sortedByEdgeWeight() {
    SortedSet<MyEntry> set = new TreeSet<>(
        Comparator.comparingInt(MyEntry::getVal));

    MyEntry entryA = new MyEntry("A", 0);
    MyEntry entryB = new MyEntry("B", 999_999_999);
    MyEntry entryC = new MyEntry("C", 2);

    set.addAll(List.of(entryA, entryB, entryC));

    // Update (remove, insert) B from B.val = 1 to B.val = 3
    // Remove B
    assertThat(set.remove(entryB)).isTrue();
    assertThat(set)
        .doesNotContain(entryB)
        .doesNotContain(new MyEntry("B", 999_999_999));

    // Insert B again.
    set.add(new MyEntry("B", 3));

    // Add some new entry (unused id, unused val in any place in set).
    set.add(new MyEntry("X", 5));

    assertThat(set)
        .contains(new MyEntry("A", 0))
        // !!! (B,2) was removed!
        .contains(new MyEntry("B", 2))
        .contains(new MyEntry("B", 3))
        .contains(new MyEntry("C", 2))
        // !!! (C, 3) was never added to the set!
        .contains(new MyEntry("C", 3))
        .contains(new MyEntry("X", 5))
        .doesNotContain(new MyEntry("B", 999_999_999));
  }
}