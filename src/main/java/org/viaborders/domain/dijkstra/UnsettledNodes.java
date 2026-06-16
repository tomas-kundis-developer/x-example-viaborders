package org.viaborders.domain.dijkstra;

import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
class UnsettledNodes {

  @Getter
  private final Map<String, Integer> nodes = new HashMap<>();

  public void addNode(@NotNull String nodeId, int distance) {
    nodes.put(nodeId, distance);
    log.debug("Adding node: nodeId={}, distance={}", nodeId, distance);
  }

  public void clear() {
    nodes.clear();
  }

  public boolean isNotEmpty() {
    return !nodes.isEmpty();
  }

  public Optional<String> removeNodeWithLowestDistance() {
    log.debug("Searching node with lowest distance ... STARTED");

    Optional<Map.Entry<String, Integer>> nodeWithLowestDistance =
        nodes.entrySet().stream()
            // TODO: 2026-06-18: TOKU: Try to find some Optional pattern to shorten this method
            .min(Map.Entry.comparingByValue());

    log.debug("Searching node with lowest distance ... DONE");

    if (nodeWithLowestDistance.isEmpty()) {
      log.debug("Unsettled nodes list is empty.");
      return Optional.empty();
    }

    String nodeId = nodeWithLowestDistance.get().getKey();
    nodes.remove(nodeId);

    log.debug("Removing node with lowest distance: {}", nodeWithLowestDistance.get());

    return Optional.of(nodeId);
  }

  public void updateNodeDistance(@NotNull String nodeId, int distance) {
    nodes.replace(nodeId, distance);
    log.debug("Updating node distance: nodeId: {}, new distance={}", nodeId, distance);
  }
}
