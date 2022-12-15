package visibilityline;

import api.Animation;
import api.AreaAlgorithm;
import geometri.Area;
import geometri.Edge;
import geometri.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


/**
 * The idea behind this method is to generate all visibility lines, then creating a grid,
 * and then reduce the edges/vertexes by merging two vertexes close to each other into one.
 * <pre>
 * **Steps**
 * - Generate all visibility lines(edges)
 * - Find all intersection points and split the two lines and create a new node
 * - Sort all lines by length in increasing order
 * - Assign a weight(w) of 1 to all nodes
 * - Merge the from(a) and to(b) node into one node(c) for the shortest line(s).
 *   - New node weight and coordinate calculation:
 *     c.w = a.w + b.w
 *     (c.x, c.y) = ((a.w * a.x + b.w * b.x)/w , (a.w * a.y + b.w * b.y)/w)
 *   - Remove the shortest line(s) from the sorted list
 *   - Replace all lines connecting node a and b with the rest of the graph, by
 *     adding a new line to c, make sure there are no duplicates.
 * - Continue until all lines are longer than N meters.
 * </pre>
 */
public class MergeVLStrategy implements AreaAlgorithm {
    private final static double minLineLengthSqrt = 40d * 40d;
    public static final String ID = "merge";

    private MergeVLStrategy() { }

    public static AreaAlgorithm of() {
        return new CompositeAreaAlgorithm(ID, AddVisibilityLines.of(), new MergeVLStrategy(), Area::makeAcceptedEdgesIntoCandidates);
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public void updateArea(Area area, Animation animation) {
        List<Edge> visibilityLines = new ArrayList<>(area.paths());

        for (int i = 0; i < visibilityLines.size(); ++i) {
            Edge k = visibilityLines.get(i);
            animation.step(k);
            for (int j = i+1; j < visibilityLines.size(); ++j) {
                var e2 = k.join(visibilityLines.get(j));
                if(e2.a().isSplit()) {
                    k = e2.a();
                    visibilityLines.set(i, k);
                }
                if(e2.b().isSplit()) {
                    visibilityLines.set(j, e2.b());
                }
            }
            if(!k.isSplit()) {
                k.makeAccepted();
            }
        }
        animation.endSection();

        area.makeAcceptedEdgesIntoCandidates();

        animation.startSection();
        var tree = new TreeSet<Edge>(Line.compareLength());
        tree.addAll(area.paths());

        var shortest = tree.pollFirst();

        var log = new TrackLogger();
        while (shortest != null && shortest.lengthSqrt < minLineLengthSqrt) {
            animation.step(shortest);
            log.log("%.0f %s", shortest.lengthSqrt, shortest);
            List<Edge> edges = shortest.mergePoints();
            if(!edges.isEmpty()) {
                tree.removeIf(Edge::isDisconnected);
                tree.addAll(edges);
            }
            else {
                shortest.makeAccepted();
            }
            shortest = tree.pollFirst();
        }
        // Keep the rest
        if(shortest != null) { shortest.makeAccepted(); }
        tree.forEach(n -> { if(n.isCandidate()) { n.makeAccepted(); }});
        System.out.println("Stop");
    }


    private static class TrackLogger {
        int i = 0;
        int logInterval = 1;

        private void log(String msg, Object ... args) {
            if(i % logInterval == 0) {
                System.out.println("%-5d ".formatted(i) + msg.formatted(args));
                logInterval = i < 5 ? i+1 : (i < 100 ? 10 : (i < 1000 ? 100 : 1000));
            }
            ++i;
        }
    }
}
