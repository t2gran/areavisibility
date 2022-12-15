package geometri;

/**
 * A composite edge holding references to the two new edges after the
 * edge is split. A SplitEdge is not connected with the Nodes in each end,
 * only the leaf Nodes are. A split edge can contain another SplitEdge
 * forming a tree where the leafs are regular connected edges({@link Edge}).
 */
class SplitEdge extends Edge {
  private Edge beginning;
  private Edge end;

  public SplitEdge(Edge beginning, Edge end) {
    super(beginning.from(), end.to(), Status.CANDIDATE);
    this.beginning = beginning;
    this.end = end;
  }

  public Edge beginning() {
    return beginning;
  }

  public Edge end() {
    return end;
  }

  @Override
  public boolean isSplit() {
    return true;
  }

  @Override
  protected SplitEdge split(Node c) {
    if(beginning.hasPossibleIntersectionPoint(c)) {
      this.beginning = this.beginning.split(c);
    }
    else {
      this.end = this.end.split(c);
    }
    return this;
  }

  @Override
  public Edge copyFixed(NodeFactory nodeFactory) {
    return new SplitEdge(beginning.copyFixed(nodeFactory), end.copyFixed(nodeFactory));
  }
}
