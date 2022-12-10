package geometri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Edge extends Line {
    boolean fixed;

    Edge(Node from, Node to, boolean fixed) {
        super(from, to);
        this.fixed = fixed;
        from.connect(this);
        to.connect(this);
    }

    public boolean fixed() {
        return fixed;
    }

    public Node from() {
        return (Node)a;
    }

    public Node to() {
        return (Node)b;
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
