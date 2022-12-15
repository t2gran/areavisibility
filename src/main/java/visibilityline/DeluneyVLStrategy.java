package visibilityline;

import api.Animation;
import api.AreaAlgorithm;
import geometri.Area;
import geometri.Edge;
import geometri.Line;

import java.util.ArrayList;
import java.util.List;

public class DeluneyVLStrategy implements AreaAlgorithm {

    public static final String ID = "deluney";
    public Area area;

    private DeluneyVLStrategy() { }

    public static AreaAlgorithm of() {
        return new CompositeAreaAlgorithm(ID, AddVisibilityLines.of(), new DeluneyVLStrategy(), Area::makeAcceptedEdgesIntoCandidates);
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public void updateArea(
            Area area,
            Animation animation
    ) {
        this.area = area;
        List<Edge> acceptedLines = new ArrayList<>();
        List<Edge> visibilityLines = new ArrayList<>(area.paths());
        visibilityLines.sort(Line.compareLength());

        for (var it : visibilityLines) {
            if(doNotIntersect(it, acceptedLines)) {
                it.makeAccepted();
                acceptedLines.add(it);
            }
            animation.step(it);
        }
    }

    boolean doNotIntersect(Edge edge, List<Edge> acceptedEdges) {
        return acceptedEdges.stream().noneMatch(edge::intersect);
    }
}
