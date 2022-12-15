package geometri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Area {
    private final String name;
    private final Polygon boarder;
    private final List<Polygon> holes;
    private final List<Node> nodes;
    private final List<Edge> edges;

    // Points inside the area witch can assist in generating VLs
    private final List<Node> extraPoints = new ArrayList<>();

    // The Animation display iterate over edges, so we need to synchronize this with any modifications to
    // the same list.
    private final Object lock = new Object();

    public Area(Area other, NodeFactory nodeFactory) {
        this.name = other.name;
        this.boarder = other.boarder.copy(nodeFactory);
        this.holes = other.holes.stream().map(p -> p.copy(nodeFactory)).toList();
        this.nodes = nodesFromPolygons(this.boarder, this.holes);
        this.edges = edgesFromPolygons(this.boarder, this.holes);
    }

    @SafeVarargs
    public Area(String name, List<Point> boarder, List<Point> ... holes) {
        this.name = name;
        this.boarder = Edge.createPolygon(boarder);
        this.holes = Arrays.stream(holes)
                .map(Edge::createPolygon)
                .collect(Collectors.toList());
        this.nodes = nodesFromPolygons(this.boarder, this.holes);
        this.edges = edgesFromPolygons(this.boarder, this.holes);
    }

    public String name() {
        return name;
    }

    public Polygon boarder() { return boarder; }

    public List<Polygon> holes() {
        return holes;
    }

    public List<Node> nodes() { return nodes; }

    public List<Edge> edges() { return edges; }

    public List<Node> extraPoints() {
        return extraPoints;
    }

    /** Path across the area, grid of visibility lines. */
    public Collection<Edge> paths() {
        synchronized (lock) {
            var paths = new HashSet<Edge>();
            var nodesVisited = new HashSet<Node>();
            var queue = new LinkedList<>(nodes);

            while (!queue.isEmpty()) {
                var next = queue.pop();
                nodesVisited.add(next);
                for (Edge edge : next.edges) {
                    addEdgeToPath(edge, paths, nodesVisited, queue);
                }
            }
            return paths;
        }
    }

    public void removePathsButKeepIntersectionPoints() {
        synchronized (lock) {
            this.extraPoints.addAll(pathNodes().stream().map(Node::floating).toList());
            removeAllPaths();
        }
    }

    /** Delete all candidate edges and make all accepted edges a candidate */
    public void makeAcceptedEdgesIntoCandidates() {
        synchronized (lock) {
            var paths = paths();
            for (Edge edge : paths) {
                if (edge.isAccepted()) {
                    edge.makeCandidate();
                } else if(edge.isCandidate()) {
                    edge.disconnect();
                }
            }
        }
    }
    public void removeCandidates() {
        synchronized (lock) {
            var paths = paths();
            for (Edge edge : paths) {
                if(edge.isCandidate()) {
                    edge.disconnect();
                }
            }
        }
    }



    /** Path across the area, grid of visibility lines. */
    public Collection<Node> pathNodes() {
        var nodes = new HashSet<Node>();
        for (Edge path : paths()) {
            var n = path.from().fixed() ? path.to() : path.from();
            if(!n.fixed()) {
                nodes.add(n);
            }
        }
        return nodes;
    }

    public void removeAllPaths() {
        synchronized (lock) {
            for (Node node : nodes) {
                node.disconnectAllFloatingEdges();
            }
        }
    }

    public boolean intersectWith(Edge edge) {
        return edges.stream().anyMatch(edge::intersect);
    }

    public Area copy(NodeFactory factory) {
        return new Area(this, factory);
    }

    private void addEdgeToPath(Edge edge, HashSet<Edge> paths, HashSet<Node> nodesVisited, LinkedList<Node> queue) {
        if(edge.isFixed()) { return; }
        if(edge instanceof SplitEdge se) {
            addEdgeToPath(se.beginning(), paths, nodesVisited, queue);
            addEdgeToPath(se.end(), paths, nodesVisited, queue);
        }
        else if(paths.add(edge)) {
            visit(edge.from(), nodesVisited, queue);
            visit(edge.to(), nodesVisited, queue);
        }
    }

    private static void visit(Node c, Set<Node> nodesVisited, List<Node> queue) {
        if(!nodesVisited.contains(c)) {
            queue.add(c);
        }
    }


    private static List<Node> nodesFromPolygons(Polygon main, Collection<Polygon> holes) {
        var list = new ArrayList<>(main.points());
        holes.forEach(p -> list.addAll(p.points()));
        return List.copyOf(list);
    }
    private static List<Edge> edgesFromPolygons(Polygon main, Collection<Polygon> holes) {
        var list = new ArrayList<>(main.boarderLines());
        holes.forEach(p -> list.addAll(p.boarderLines()));
        return List.copyOf(list);
    }
}
