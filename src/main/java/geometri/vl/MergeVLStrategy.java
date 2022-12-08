package geometri.vl;

import geometri.AddVLStrategy;
import geometri.Area;
import geometri.Line;
import geometri.Node;

import java.util.List;
import java.util.function.Consumer;

public class MergeVLStrategy implements AddVLStrategy {
    @Override
    public void addVisibilityLines(
            Area area,
            List<Line<Node>> visibilityLines,
            Consumer<Line<Node>> step
    ) {
        visibilityLines.sort(Line.compareLength());
        for (Line<Node> it : visibilityLines) {
            step.accept(it);
            if(!area.intersectEdges(it)) {
                area.addVisibilityEdge(it.a, it.b);
            }
        }
    }
}
