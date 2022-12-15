package visibilityline;

import api.Animation;
import api.AreaAlgorithm;
import geometri.Area;
import geometri.Bound;
import geometri.Edge;
import geometri.Node;
import geometri.Polygon;

import java.util.Collection;
import java.util.List;

/**
 * Adding all visibility to an area.
 */
public class AddVisibilityLines implements AreaAlgorithm {
    private Area area;
    private Animation animation;

    private AddVisibilityLines() {}

    public static AreaAlgorithm of() {
        return new AddVisibilityLines();
    }

    @Override
    public String id() {
        return "vl";
    }

    @Override
    public String name() {
        return "add visibility lines";
    }

    @Override
    public void updateArea(Area area, Animation animation) {
        this.area = area;
        this.animation = animation;

        Polygon main = area.boarder();

        // Generate VL inside the main polygon
        generateVLines(main, Bound.INSIDE);

        // Generate VL along the edge outside each hole
        for (var hole : area.holes()) {
            generateVLines(hole, Bound.OUTSIDE);
        }

        // Generate VL between each point in the main polygon and the holes
        for (var hole : area.holes()) {
            generateVLinesP2P(main.points(), hole.points());
        }

        // Generate VL for the extra points
        generateVLinesP2P(main.points(), area.extraPoints());
        generateVLines(area.extraPoints());
        for (var hole : area.holes()) {
            generateVLinesP2P(hole.points(), area.extraPoints());
        }

        // Generate VL between the holes
        for (int i=0; i<area.holes().size(); ++i) {
            var hole = area.holes().get(i);
            for (int j = i + 1; j < area.holes().size(); ++j) {
                generateVLinesP2P(hole.points(), area.holes().get(j).points());
            }
        }
    }

    private void generateVLinesP2P(Collection<Node> p1, Collection<Node> p2) {
        for (Node v : p1) {
            for (Node u : p2) {
                var line = Edge.ofDisconnected(u, v);
                if(!area.intersectWith(line)) {
                    line.makeAccepted();
                }
                animation.step(line);
            }
        }
    }

    private void generateVLines(Polygon polygon, Bound bound) {
        var lines = polygon.boarderLines();
        int jSize = lines.size()-1;
        Edge a, b, c, d;
        a = lines.get(lines.size()-1);

        for (int i = 0; i < lines.size()-2; ++i) {
            b = lines.get(i);
            c = lines.get(i+1);
            for (int j = i + 2; j < jSize; ++j) {
                d = lines.get(j);
                var line = Edge.ofDisconnected(a.to(), c.to());
                animation.step(line);
                if (acceptLine(line, a, b, c, d, bound)) {
                    line.makeAccepted();
                }
                c = d;
            }
            a = b;
            jSize = lines.size();
        }
    }

    private void generateVLines(List<Node> nodes) {
        for (int i = 0; i < nodes.size()-1; ++i) {
            var a = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); ++j) {
                var line = Edge.ofDisconnected(a, nodes.get(j));
                animation.step(line);
                if (!area.intersectWith(line)) {
                    line.makeAccepted();
                }
            }
        }
    }

    private boolean acceptLine(
            Edge line,
            Edge a,
            Edge b,
            Edge c,
            Edge d,
            Bound bound
            ) {
        if(bound == Bound.INSIDE) {
            if(line.outside(a, b, c, d)) { return false; }
        }
        else {
            if(line.inside(a, b, c, d)) { return false; }
        }
        return !area.intersectWith(line);
    }
}
