import api.AreaAlgorithm;
import geometri.Area;
import view.Simulator;
import visibilityline.AddVisibilityLines;
import visibilityline.DeluneyVLStrategy;
import visibilityline.MergeAndDeluneyStrategy;
import visibilityline.MergeVLStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



// Algorithm outline
// 1. Remove points that is less than 5m from straight line between neighbors
// 2. Connect nodes that is closer to each other than to neighbors
// 3. Connect all inner Polygons most pointed corners to neighbor polygon.
// -> Now, all polygons should be convex
// 4. Connect closest "next" neighbors

// Deluney


public class Main {

    private static final List<AreaAlgorithm> ADD_VL_STRATEGIES = List.of(
      AddVisibilityLines.of(), DeluneyVLStrategy.of(), MergeVLStrategy.of(), MergeAndDeluneyStrategy.of()
    );


    public static void main(String[] args) {
        Locale.setDefault(Locale.CANADA);

        var vlStrategies = new ArrayList<AreaAlgorithm>();
        var areas = new ArrayList<Area>();

        for (String arg : args) {
            for (AreaAlgorithm it : ADD_VL_STRATEGIES) {
                if (it.id().equalsIgnoreCase(arg)) {
                    vlStrategies.add(it);
                }
            }
            for (Area area : Samples.ALL) {
                if(area.name().equalsIgnoreCase(arg)) {
                    areas.add(area);
                }
            }
        }

        if(vlStrategies.isEmpty()) {
            vlStrategies.addAll(ADD_VL_STRATEGIES);
        }
        if(areas.isEmpty()) {
            areas.addAll(Samples.ALL);
        }

        for(var vlStrategy : vlStrategies) {
            for (Area area : areas) {
                run(area, vlStrategy);
            }
        }
    }

    private static void run(Area area, AreaAlgorithm vlStrategy) {
        new Thread(area.name() + " " + vlStrategy.name()) {
            @Override
            public void run() {
                new Simulator(area, vlStrategy).run();
            }
        }.start();
    }
}
