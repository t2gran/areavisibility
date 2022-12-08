package geometri.vl;

import geometri.AddVLStrategy;
import geometri.Area;
import geometri.Line;
import geometri.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AllVLStrategy implements AddVLStrategy {
    @Override
    public void addVisibilityLines(
            Area area,
            List<Line<Node>> visibilityLines,
            Consumer<Line<Node>> step
    ) {
        List<Line<Node>> acceptedLines = new ArrayList<>();
        visibilityLines.sort(Line.compareLength());

        for (Line<Node> it : visibilityLines) {
            step.accept(it);
            area.addVisibilityEdge(it.a, it.b);
        }
    }
}