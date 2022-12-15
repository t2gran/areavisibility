package geometri;

import java.util.Collection;
import java.util.List;
import java.util.function.ToIntFunction;

public class Polygon {
    private final List<Edge> edges;

    public Polygon(Collection<Edge> edges) {
        this.edges = List.copyOf(edges);
    }

    public int[] coordinates(ToIntFunction<Point> map) {
        return edges.stream().mapToInt(l ->  map.applyAsInt(l.a)).toArray();
    }

    public List<Node> points() {
        return edges.stream().map(Edge::from).toList();
    }

    public List<Edge> boarderLines() {
        return edges;
    }

    public int size() {
        return edges.size();
    }

    public boolean intersect(Edge line) {
        return edges.stream().anyMatch(l -> l.intersect(line));
    }

    public Polygon copy(NodeFactory nodeFactory) {
        return new Polygon(edges.stream().map(e -> e.copyFixed(nodeFactory)).toList());
    }
}
