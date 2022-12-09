package geometri;

import java.util.Objects;

public class Point {
    public final double x;
    public final double y;

    protected Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point v2(double x, double y) {
        return new Point(x,y);
    }

    public Point plus(Point l) {
        return v2(x + l.x, y + l.y);
    }

    public Point transform(double c) {
        return v2(x + c, y + c);
    }

    public Point minus() {
        return v2(-x, -y);
    }

    public Point minus(Point l) {
        return v2(x - l.x, y - l.y);
    }

    public Point minus(double c) {
        return v2(x - c, y - c);
    }

    public Point dot(Point l) {
        return v2(x * l.x, y * l.y);
    }

    public Point scale(double c) {
        return v2(x * c, y * c);
    }

    public Point div(Point l) {
        return v2(x / l.x, y / l.y);
    }

    public Point div(double c) {
        return v2(x / c, y / c);
    }


    @Override
    public String toString() {
        return String.format("[%.1f, %.1f]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Point)) { return false; }
        final Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
