package geometri;

import java.util.HashMap;
import java.util.Map;

public class NodeFactory {
  private final Map<Node, Node> newNodes = new HashMap<>();

  Node of(Node old) {
    return old.fixed ? newNodes.computeIfAbsent(old, Node::new) : null;
  }
}
