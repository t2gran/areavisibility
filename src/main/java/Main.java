import geometri.Area;
import view.Simulator;

import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.CANADA);
        int i=0;
        if(i>0) {
            runInLoop("P" + i, Samples.ALL.get(i-1));
        }
        else {
            for (Area area : Samples.ALL) {
                runInLoop("P" + ++i, area);
            }
        }

        // Algorithm outline
        // 1. Remove points that is less than 5m from straight line between neighbors
        // 2. Connect nodes that is closer to each other than to neighbors
        // 3. Connect all inner Polygons most pointed corners to neighbor polygon.
        // -> Now, all polygons should be convex
        // 4. Connect closest "next" neighbors

        // Deluney

    }

    private static void runInLoop(String id, Area area) {
        //while (true) {
            var simulator = new Simulator(id, area);
            simulator.run(false);
        //    if(main.ctrl == CtrlState.PLAY) { return;}
        //}
    }
}
