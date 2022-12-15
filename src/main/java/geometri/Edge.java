package geometri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Edge extends Line {
    Status status;

    protected Edge(Node from, Node to, Status status) {
        super(from, to);
        this.status = status;
    }

    public static Edge ofFixed(Node from, Node to) {
        return new Edge(from, to, Status.FIXED).connect();
    }

    public static Edge ofAccepted(Node from, Node to) {
        return new Edge(from, to, Status.ACCEPTED).connect();
    }
    public static Edge ofDisconnected(Node from, Node to) {
        return new Edge(from, to, Status.DISCONNECTED);
    }


    public boolean isFixed() {
        return status.isFixed();
    }
    public boolean isCandidate() {
        return status.isCandidate();
    }
    public boolean isAccepted() {
        return status.isAccepted();
    }
    public boolean isDisconnected() {
        return status.isDisconnected();
    }
    public boolean isEditable() {
        return isAccepted() || isCandidate();
    }

    public Status status() {
        return status;
    }

    public void makeCandidate() {
        updateStatus(Status.CANDIDATE);
    }

    public void makeAccepted() {
        updateStatus(Status.ACCEPTED);
    }

    public Node from() {
        return (Node)a;
    }

    public Node to() {
        return (Node)b;
    }

    public Edge connect() {
        // Avoid adding an edge witch exist
        var optEdge = from().findEdge(to());
        if(optEdge.isEmpty()) {
            optEdge = to().findEdge(from());
        }
        if(optEdge.isEmpty()) {
            from().connect(this);
            to().connect(this);
            return this;
        }
        Edge edge = optEdge.get();
        if(status.isAccepted()) {
            edge.makeAccepted();
        }
        status = Status.DISCONNECTED;
        return edge;
    }

    public void disconnect() {
        this.status = Status.DISCONNECTED;
        from().disconnect(this);
        to().disconnect(this);
    }


    public boolean isSplit() {
        return false;
    }

    public EdgePair join(Edge other) {
        return join(this, other);
    }

    public Edge copyFixed(NodeFactory nodeFactory) {
        return isFixed() ? Edge.ofFixed(nodeFactory.of(from()), nodeFactory.of(to())) : null;
    }

    @Override
    public String toString() {
        return "Edge<" + a + " " + b + " " + status + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Edge edge)) { return false; }
        return Objects.equals(a, edge.a) && Objects.equals(b, edge.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    static Polygon createPolygon(List<Point> points) {
        List<Edge> edges = new ArrayList<>();
        List<Node> nodes = points
          .stream()
          .map(Node::fixed)
          .toList();
        for (int i = 0; i < nodes.size(); i++) {
            Node head = nodes.get(i);
            Node tail = nodes.get((i+1)% nodes.size());
            edges.add(Edge.ofFixed(head, tail));
        }
        return new Polygon(edges);
    }

    private static EdgePair join(Edge a, Edge b) {
        if(a.isFixed() || b.isFixed()) {
            return new EdgePair(a, b);
        }
        var ip = a.intersectionPoint(b);
        if(ip.onLineA().doesNotExist() || ip.onLineB().doesNotExist()) {
            return new EdgePair(a, b);
        }
        if(ip.onLineA().inMiddle() && ip.onLineB().inMiddle()) {
            var c = Node.floating(ip.point());
            return new EdgePair(a.split(c), b.split(c));
        }
        else if(ip.onLineA().inMiddle()) {
            return new EdgePair(a.split(ip.onLineB().inBeginning() ? b.from() : b.to()), b);
        }
        else {
            return new EdgePair(a, b.split(ip.onLineA().inBeginning() ? a.from() : a.to()));
        }
    }

    protected SplitEdge split(Node c) {
        disconnect();
        // Check if the node exist
        var beginning = c.findEdge(from()).orElse(Edge.ofAccepted(from(), c));
        var end = c.findEdge(to()).orElse(Edge.ofAccepted(c, to()));
        return new SplitEdge(beginning, end);
    }

    public List<Edge> mergePoints() {
        if(isEditable()) {
            return from().merge(to()).map(n -> (List<Edge>)n.edges).orElse(List.of());
        }
        return List.of();
    }

    private void updateStatus(Status newStatus) {
        if(isFixed()) {
            return;
        }
        if(isDisconnected()) {
            if(this == connect()) {
                this.status = newStatus;
            }
        }
        else {
            this.status = newStatus;
        }
    }
}
