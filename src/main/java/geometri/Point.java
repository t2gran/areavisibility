package geometri;

import java.util.Objects;

/**
 * Immutable
 */
public class Point {
    public final double x;
    public final double y;

    protected Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point p(double x, double y) {
        return new Point(x,y);
    }

    public Point plus(Point l) {
        return p(x + l.x, y + l.y);
    }

    public Point transform(double c) {
        return p(x + c, y + c);
    }

    public Point minus() {
        return p(-x, -y);
    }

    public Point minus(Point l) {
        return p(x - l.x, y - l.y);
    }

    public Point minus(double c) {
        return p(x - c, y - c);
    }

    public Point dot(Point l) {
        return p(x * l.x, y * l.y);
    }

    public Point scale(double c) {
        return p(x * c, y * c);
    }

    public Point div(Point l) {
        return p(x / l.x, y / l.y);
    }

    public Point div(double c) {
        return p(x / c, y / c);
    }

    public Point copy() {
        return this;
    }

    @Override
    public String toString() {
        return String.format("[%.1f, %.1f]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Point otherPoint)) { return false; }
        return Double.compare(otherPoint.x, x) == 0 && Double.compare(otherPoint.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
