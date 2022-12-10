package geometri;

import java.util.ArrayList;
import java.util.Objects;

public class Node extends Point {
    ArrayList<Edge> edges = new ArrayList<>();
    final boolean fixed;

    Node(Point v, boolean fixed) {
        super(v.x, v.y);
        this.fixed = fixed;
    }

    public boolean fixed() {
        return fixed;
    }

    void connect(Edge edge) {
        edges.add(edge);
    }

    @Override
    public String toString() {
        return String.format("[%.1f, %.1f]", x, y);
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
}
