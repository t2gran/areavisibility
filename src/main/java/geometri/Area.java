package geometri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Area {
    private final Polygon<Node, Edge> boarder;
    private final List<Polygon<Node, Edge>> holes;
    private final List<Node> nodes = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();


    @SuppressWarnings("unchecked")
    public Area(List<V2> boarder, List<V2> ... holes) {
        this.boarder = Edge.createPolygon(boarder);
        this.holes = Arrays.stream(holes)
                .map(Edge::createPolygon)
                .collect(Collectors.toList());
        addPolygonToEdgesAndNodes(this.boarder);
        this.holes.forEach(this::addPolygonToEdgesAndNodes);
    }

    public Polygon<Node, Edge> boarder() { return boarder; }

    public List<Polygon<Node, Edge>> holes() {
        return holes;
    }

    public List<Node> nodes() { return nodes; }

    public List<Edge> edges() { return edges; }

    public boolean intersectEdges(Line<?> line) {
        return edges.stream().anyMatch(e -> line.intersect(e));
    }

    public void addVisibilityEdge(Node a, Node b) {
        edges.add(new Edge(a, b, false));
    }

    private void addPolygonToEdgesAndNodes(Polygon<Node, Edge> polygon) {
        for (Edge edge : polygon.boarderLines()) {
            this.nodes.add(edge.a);
            this.edges.add(edge);
        }
    }
}
