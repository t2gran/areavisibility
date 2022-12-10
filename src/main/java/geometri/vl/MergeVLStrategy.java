package geometri.vl;

import geometri.AddVLStrategy;
import geometri.Area;
import geometri.Edge;
import geometri.Line;

import java.util.List;
import java.util.function.Consumer;

public class MergeVLStrategy implements AddVLStrategy {
    @Override
    public String name() {
        return "merge";
    }

    @Override
    public void addVisibilityLines(
            Area area,
            List<Edge> visibilityLines,
            Consumer<Edge> step
    ) {
        visibilityLines.sort(Line.compareLength());
        for (Edge it : visibilityLines) {
            step.accept(it);
            if(!area.intersectEdges(it)) {
                area.addVisibilityEdge(it.from(), it.to());
            }
        }
    }
}
