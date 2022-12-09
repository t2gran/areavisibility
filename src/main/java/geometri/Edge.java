package geometri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Edge extends Line<Node> {
    boolean fixed;

    Edge(Node a, Node b, boolean fixed) {
        super(a, b);
        this.fixed = fixed;
        a.connect(this);
        b.connect(this);
    }

    public boolean fixed() {
        return fixed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (!(o instanceof Edge)) {return false;}
        final Edge edge = (Edge) o;
        return Objects.equals(a, edge.a) && Objects.equals(b, edge.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    static Polygon<Node, Edge> createPolygon(List<Point> points) {
        List<Edge> edges = new ArrayList<>();
        List<Node> nodes = points.stream()
                .map(p -> new Node(p, true))
                .collect(Collectors.toList());
        for (int i = 0; i < nodes.size(); i++) {
            Node head = nodes.get(i);
            Node tail = nodes.get((i+1)% nodes.size());
            edges.add(new Edge(head, tail, true));
        }
        return new Polygon<>(edges);
    }

}
