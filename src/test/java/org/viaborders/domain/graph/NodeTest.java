package org.viaborders.domain.graph;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
class NodeTest {

  @Autowired
  private Validator validator;

  @Test
  void test() {
    Node nodeA = new Node("test-node-A");

    assertThat(nodeA.getId()).isEqualTo("test-node-A");
    assertThat(nodeA.getEdges()).isEmpty();

    nodeA.addEdge(new Edge("test-node-B", 11));

    var edges = nodeA.getEdges();
    assertThat(edges).hasSize(1);

    var edgeNode = edges.poll();

    assertThat(edgeNode.getTargetNodeId()).isEqualTo("test-node-B");
    assertThat(edgeNode.getWeight()).isEqualTo(11);
  }

  @Test
  void test_idNull() {
    Set<ConstraintViolation<Node>> violations = validator.validate(new Node(null));

    assertThat(violations).hasSize(1);

    ConstraintViolation<Node> violation = violations.stream().findFirst().get();

    assertThat(violation.getPropertyPath()).hasToString("id");
    assertThat(violation.getMessage()).isEqualTo("must not be blank");
  }

  @ParameterizedTest
  @ValueSource(strings = {" ", "  "})
  void test_idSpace(String id) {
    var node = new Node(id);

    Set<ConstraintViolation<Node>> violations = validator.validate(node);

    assertThat(violations).hasSize(1);

    ConstraintViolation<Node> violation = violations.stream().findFirst().get();

    assertThat(violation.getPropertyPath()).hasToString("id");
    assertThat(violation.getMessage()).isEqualTo("must not be blank");
  }

  @Test
  void testAddEdge_null() throws NoSuchMethodException {
    var node = new Node("test-node-A");

    Method method = Node.class.getMethod("addEdge", Edge.class);
    Object[] args = {null};

    Set<ConstraintViolation<Node>> violations = validator.forExecutables()
        .validateParameters(node, method, args);

    assertThat(violations).hasSize(1);

    ConstraintViolation<Node> violation = violations.stream().findFirst().get();

    assertThat(violation.getPropertyPath()).hasToString("addEdge.edge");
    assertThat(violation.getMessage()).isEqualTo("must not be null");
  }
}