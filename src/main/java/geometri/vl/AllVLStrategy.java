package geometri.vl;

import geometri.AddVLStrategy;
import geometri.Area;
import geometri.Edge;
import geometri.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AllVLStrategy implements AddVLStrategy {
    @Override
    public void addVisibilityLines(
            Area area,
            List<Edge> visibilityLines,
            Consumer<Edge> step
    ) {
        List<Edge> acceptedLines = new ArrayList<>();
        visibilityLines.sort(Line.compareLength());

        for (var it : visibilityLines) {
            step.accept(it);
            area.addVisibilityEdge(it.from(), it.to());
        }
    }
}
