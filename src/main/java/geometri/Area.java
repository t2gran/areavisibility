package geometri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Area {
    private final String name;
    private final Polygon<Node, Edge> boarder;
    private final List<Polygon<Node, Edge>> holes;
    private final List<Node> nodes = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();
    private final List<Edge> paths = new ArrayList<>();

    public Area(Area other) {
        this.name = other.name;
        this.boarder = other.boarder.copy();
        this.holes = other.holes.stream().map(Polygon::copy).toList();
        this.nodes.addAll(other.nodes.stream().map(Node::copy).toList());
        this.edges.addAll(other.edges.stream().map(Edge::copy).toList());
        this.paths.addAll(other.paths.stream().map(Edge::copy).toList());
    }

    @SuppressWarnings("unchecked")
    public Area(String name, List<Point> boarder, List<Point> ... holes) {
        this.name = name;
        this.boarder = Edge.createPolygon(boarder);
        this.holes = Arrays.stream(holes)
                .map(Edge::createPolygon)
                .collect(Collectors.toList());
        addPolygonToEdgesAndNodes(this.boarder);
        this.holes.forEach(this::addPolygonToEdgesAndNodes);
    }

    public String name() {
        return name;
    }

    public Polygon<Node, Edge> boarder() { return boarder; }

    public List<Polygon<Node, Edge>> holes() {
        return holes;
    }

    public List<Node> nodes() { return nodes; }

    public List<Edge> edges() { return edges; }

    public List<Edge> paths() { return paths; }

    public boolean intersectEdges(Edge edge) {
        return edges.stream().anyMatch(edge::intersect);
    }

    public void addVisibilityEdge(Node a, Node b) {
        paths.add(new Edge(a, b, true));
    }

    public Area copy() {
        return new Area(this);
    }

    private void addPolygonToEdgesAndNodes(Polygon<Node, Edge> polygon) {
        for (Edge edge : polygon.boarderLines()) {
            this.nodes.add(edge.from());
            this.edges.add(edge);
        }
    }
}
