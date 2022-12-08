package geometri;

import java.util.Objects;

public class V2 {
    public final double x;
    public final double y;

    protected V2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static V2 v2(double x, double y) {
        return new V2(x,y);
    }

    public V2 plus(V2 l) {
        return v2(x + l.x, y + l.y);
    }

    public V2 transform(double c) {
        return v2(x + c, y + c);
    }

    public V2 minus() {
        return v2(-x, -y);
    }

    public V2 minus(V2 l) {
        return v2(x - l.x, y - l.y);
    }

    public V2 minus(double c) {
        return v2(x - c, y - c);
    }

    public V2 dot(V2 l) {
        return v2(x * l.x, y * l.y);
    }

    public V2 scale(double c) {
        return v2(x * c, y * c);
    }

    public V2 div(V2 l) {
        return v2(x / l.x, y / l.y);
    }

    public V2 div(double c) {
        return v2(x / c, y / c);
    }


    @Override
    public String toString() {
        return String.format("[%.1f, %.1f]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof V2)) { return false; }
        final V2 v2 = (V2) o;
        return Double.compare(v2.x, x) == 0 && Double.compare(v2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
