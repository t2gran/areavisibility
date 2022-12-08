package geometri;

import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Polygon<S extends V2, T extends Line<S>> {
    private final List<T> lines;

    public Polygon(List<T> lines) {
        this.lines = lines;
    }

    public int[] coordinates(ToIntFunction<S> map) {
        return lines.stream().mapToInt(l ->  map.applyAsInt(l.a)).toArray();
    }

    public List<S> points() {
        return lines.stream().map(l -> l.a).collect(Collectors.toList());
    }

    public List<T> boarderLines() {
        return lines;
    }

    public int size() {
        return lines.size();
    }

    public boolean intersect(T line) {
        return lines.stream().anyMatch(l -> l.intersect(line));
    }
}
