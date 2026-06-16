package org.viaborders.domain.dijkstra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
class UnsettledNodesTest {

  @Test
  void removeNodeWithLowestDistance() {

    var unsettledNodes = new UnsettledNodes();
    unsettledNodes.addNode("A", 3);
    unsettledNodes.addNode("B", 1);
    unsettledNodes.addNode("C", 2);
    unsettledNodes.addNode("D", 4);

    assertThat(unsettledNodes.removeNodeWithLowestDistance())
        .get()
        .isEqualTo("B");

    assertThat(unsettledNodes.removeNodeWithLowestDistance())
        .get()
        .isEqualTo("C");

    assertThat(unsettledNodes.removeNodeWithLowestDistance())
        .get()
        .isEqualTo("A");

    assertThat(unsettledNodes.removeNodeWithLowestDistance())
        .get()
        .isEqualTo("D");

    assertThat(unsettledNodes.removeNodeWithLowestDistance())
        .isEmpty();

    assertThat(unsettledNodes.removeNodeWithLowestDistance())
        .isEmpty();
  }

  @Test
  void removeNodeWithLowestDistance_moreMinValues() {

    var unsettledNodes = new UnsettledNodes();
    unsettledNodes.addNode("A", 1);
    unsettledNodes.addNode("B", 1);

    Optional<String> resultOpt = unsettledNodes.removeNodeWithLowestDistance();

    assertThat(resultOpt)
        .isNotEmpty();

    String result = resultOpt.get();

    assertThat(result.equals("A") || result.equals("B"))
        .isTrue();
  }

  @Test
  void removeNodeWithLowestDistance_empty() {

    var unsettledNodes = new UnsettledNodes();

    assertThat(unsettledNodes.removeNodeWithLowestDistance())
        .isEmpty();
  }
}