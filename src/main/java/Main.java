import geometri.AddVLStrategy;
import geometri.Area;
import geometri.vl.AllVLStrategy;
import geometri.vl.DeluneyVLStrategy;
import view.Simulator;

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

    private static final List<AddVLStrategy> ADD_VL_STRATEGIES = List.of(
      new AllVLStrategy(), new DeluneyVLStrategy() /*, new MergeVLStrategy()*/
    );


    public static void main(String[] args) {
        Locale.setDefault(Locale.CANADA);

        var vlStrategies = new ArrayList<AddVLStrategy>();
        var areas = new ArrayList<Area>();

        for (String arg : args) {
            for (AddVLStrategy it : ADD_VL_STRATEGIES) {
                if (it.name().equalsIgnoreCase(arg)) {
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

    private static void run(Area area, AddVLStrategy vlStrategy) {
        new Thread(area.name() + " " + vlStrategy.name()) {
            @Override
            public void run() {
                new Simulator(area, vlStrategy).run();
            }
        }.start();
    }
}
