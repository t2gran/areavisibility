package api;

import geometri.Area;

/**
 * Algorithm to modify the Area.
 */
public interface AreaAlgorithm {
    String id();

    default String name() {
        return id();
    }

    void updateArea(Area area, Animation animation);
}
