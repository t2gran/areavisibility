package geometri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Node extends Point {
    final ArrayList<Edge> edges = new ArrayList<>();
    final boolean fixed;
    int weight;

    private Node(double x, double y, boolean fixed, int weight) {
        super(x, y);
        this.fixed = fixed;
        this.weight = weight;
    }

    Node(Node other) {
        this(other.x, other.y, other.fixed, other.weight);
    }
    public static Node fixed(Point p) {
        return new Node(p.x, p.y, true, 1);
    }

    public static Node floating(Point p) {
        return new Node(p.x, p.y, false, 1);
    }

    public boolean fixed() {
        return fixed;
    }

    void connect(Edge edge) {
        edges.add(edge);
    }

    void disconnect(Edge edge) {
        edges.remove(edge);
    }

    public void disconnectAllFloatingEdges() {
        for (Edge edge :  List.copyOf(edges)) {
            if(edge.isEditable()) {
                this.disconnect(edge);
            }
        }
    }

    public Node copy(NodeFactory nodeFactory) {
        return fixed ? nodeFactory.of(this) : null;
    }

    public Optional<Edge> findEdge(Node fromOrTo) {
        for (Edge e : edges) {
            if(e.from() == fromOrTo || e.to() == fromOrTo) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public Optional<Node> merge(Node other) {
        if(fixed && other.fixed) {
            return Optional.empty();
        }
        // Find new weighted
        Node c;
        if(fixed) {
            c = this;
        }
        else if(other.fixed) {
            c = other;
        }
        else {
            int totWeight = weight + other.weight;
            var p = scale(weight).plus(other.scale(other.weight)).div(totWeight);
            c = new Node(p.x, p.y, false, 0);
        }
        // Merge existing nodes into new node
        c.mergeIn(this);
        c.mergeIn(other);
        return Optional.of(c);
    }

    @Override
    public String toString() {
        return String.format("(%.1f %.1f" + (fixed? ")" : ")?"), x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Node oNode)) { return false; }
        return Double.compare(oNode.x, x) == 0 && Double.compare(oNode.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    private void mergeIn(Node other) {
        if(other.fixed) { return; }

        this.weight += other.weight;
        // Make copy to be able to remove elements in original list
        var otherEdges = List.copyOf(other.edges);

        for(var edge : otherEdges){
            if(edge.from() == other) {
                Edge.ofAccepted(this, edge.to());
                edge.disconnect();
            }
            else {
                Edge.ofAccepted(edge.from(), this);
                edge.disconnect();
            }
        };
    }
}
