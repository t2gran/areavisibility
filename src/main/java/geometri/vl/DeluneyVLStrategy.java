package geometri.vl;

import geometri.AddVLStrategy;
import geometri.Area;
import geometri.Edge;
import geometri.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DeluneyVLStrategy implements AddVLStrategy {

    @Override
    public String name() {
        return "deluney";
    }

    @Override
    public void addVisibilityLines(
            Area area,
            List<Edge> visibilityLines,
            Consumer<Edge> step
    ) {
        List<Edge> acceptedLines = new ArrayList<>();
        visibilityLines.sort(Line.compareLength());

        for (var it : visibilityLines) {
            if(noneIntersect(it, acceptedLines)) {
                acceptedLines.add(it);
                area.addVisibilityEdge(it.from(), it.to());
            }
        }
    }

    boolean noneIntersect(Edge edge, List<Edge> acceptedEdges) {
        return acceptedEdges.stream().noneMatch(edge::intersect);
    }
}
