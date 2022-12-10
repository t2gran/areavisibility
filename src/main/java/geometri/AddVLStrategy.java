package geometri;

import java.util.List;
import java.util.function.Consumer;

public interface AddVLStrategy {
    String name();

    void addVisibilityLines(Area area, List<Edge> visibilityLines, Consumer<Edge> step);
}
