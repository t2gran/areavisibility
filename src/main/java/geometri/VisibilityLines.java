package geometri;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class VisibilityLines {
    private final Area area;
    private final List<Line<Node>> visibilityLines = new ArrayList<>();
    private final AddVLStrategy addVLStrategy;


    public VisibilityLines(Area area, AddVLStrategy addVLStrategy) {
        this.area = area;
        this.addVLStrategy = addVLStrategy;
    }

    Area area() {
        return area;
    }

    List<Line<Node>> visibilityLines() {
        return visibilityLines;
    }

    public void generate(Consumer<Line<Node>> step) {
        Polygon<Node, Edge> main = area.boarder();
        generateVLines(main, step, Bound.INSIDE);

        for (int i=0; i<area.holes().size(); ++i) {
            var hole = area.holes().get(i);
            generateVLines(hole, step, Bound.OUTSIDE);
            generateVLinesP2P(main, hole, step);
            for (int j = i + 1; j < area.holes().size(); ++j) {
                generateVLinesP2P(hole, area.holes().get(j), step);
            }
        }
        addVLStrategy.addVisibilityLines(area, visibilityLines, step);
    }

    private void generateVLinesP2P(Polygon<Node, Edge> p1, Polygon<Node, Edge> p2, Consumer<Line<Node>> step) {
        for (Node v : p1.points()) {
            for (Node u : p2.points()) {
                Line<Node> line = new Line<>(u, v);
                step.accept(line);
                if(!area.intersectEdges(line)) {
                    add(line);
                }
            }
        }
    }

    private void generateVLines(
            Polygon<Node, Edge> polygon,
            Consumer<Line<Node>> step,
            Bound bound
    ) {
        var lines = polygon.boarderLines();
        int jSize = lines.size()-1;
        Edge a, b, c, d;
        a = lines.get(lines.size()-1);

        for (int i = 0; i < lines.size()-2; ++i) {
            b = lines.get(i);
            c = lines.get(i+1);
            for (int j = i + 2; j < jSize; ++j) {
                d = lines.get(j);
                var line = new Line<>(a.b, c.b);
                step.accept(line);

                if (acceptLine(line, a, b, c, d, bound)) {
                    add(line);
                }
                c = d;
            }
            a = b;
            jSize = lines.size();
        }
    }

    private boolean acceptLine(
            Line<?> line,
            Line<?> a,
            Line<?> b,
            Line<?> c,
            Line<?> d,
            Bound bound
            ) {
        if(bound == Bound.INSIDE) {
            if(line.outside(a, b, c, d)) { return false; }
        }
        else {
            if(line.inside(a, b, c, d)) { return false; }
        }
        return !area.intersectEdges(line);
    }

    private void add(Line<Node> l) {
        visibilityLines.add(l);
    }
}