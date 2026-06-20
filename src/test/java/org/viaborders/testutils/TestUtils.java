package org.viaborders.testutils;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.viaborders.domain.graph.Graph;

/**
 * Utilities for test classes only.
 */
@UtilityClass
@Log4j2
public class TestUtils {

  /**
   * Log graph structure.
   */
  public void logGraph(Graph graph) {
    log.debug("Graph to test:\n{}", graph);
  }
}
