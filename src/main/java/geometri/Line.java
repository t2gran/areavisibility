package geometri;

import java.util.Comparator;

/**
 * Immutable
 */
public class Line {
    public final Point a;
    public final Point b;
    public final double lengthSqrt;
    public final double angleAlfaCCV;

    public Line(Point a, Point b) {
        this.a = a;
        this.b = b;
        this.angleAlfaCCV = Angle.alfaAngle(dx(), dy());
        this.lengthSqrt = dx() * dx() + dy() * dy();
    }

    Bound side(Point p) {
        var test = (p.x-a.x) * (b.y - a.y) - (p.y - a.y) * (b.x - a.x);

        if(test < -0.001) { return Bound.INSIDE; }
        if(test > 0.001) { return Bound.OUTSIDE; }
        return Bound.ON;
    }

    public double dx() {
        return b.x - a.x;
    }

    public double dy() {
        return b.y - a.y;
    }

    public double alfa() {
        return angleAlfaCCV;
    }

    public double alfaInv() {
        return Angle.alfaInverse(angleAlfaCCV);
    }

    /**
     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line_segment
     */
    public boolean intersect(Line o) {
        double dx = dx();
        double dy = dy();
        double oDy = o.dy();
        double oDx = o.dx();
        double D = dx * oDy - dy * oDx;
        // The line s are parallel
        if(Math.abs(D) < 0.001) { return false; }

        double tx = b.x - o.b.x;
        double ty = b.y - o.b.y;

        double t = (tx * dy - ty * dx) / D;
        double u = (tx * oDy - ty * oDx) / D;

        if(t < -0.001 || t > 1.001 || u < -0.001 || u > 1.001) { return false; }
        return (t > 0.001 && t < 0.999) || (u > 0.001 && u < 0.999);

                    /* Calculate  point
            {
                // Collision detected
                if (i_x != NULL)
            *i_x = a.x + (t * s1_x);
                if (i_y != NULL)
            *i_y = a.y + (t * s1_y);
                return 1;
            }*/

    }

    public boolean inside(Line a, Line b, Line c, Line d) {
        return inside(alfa(), a, b) && inside(alfaInv(), c, d);
    }

    public boolean outside(Line a, Line b, Line c, Line d) {
        return outside(alfa(), a, b) && outside(alfaInv(), c, d);
    }

    public boolean inside(double alfa, Line a, Line b) {
        return Angle.between(alfa, a.alfaInv(), b.alfa());
    }

    public boolean outside(double alfa, Line a, Line b) {
        return Angle.notBetween(alfa, a.alfaInv(), b.alfa());
    }

    public static <S extends Point> Comparator<Line> compareLength() {
        return (o1, o2) -> (int)Math.round((o1.lengthSqrt - o2.lengthSqrt) * 100d);
    }

    private boolean overlap(double u0, double u1, double v0, double v1) {
        if(u0 > u1) {
            double temp = u0;
            u0 = u1;
            u1 = temp;
        }
        if(v0 > v1) {
            double temp = v0;
            v0 = v1;
            v1 = temp;
        }
        return u0 > v1 || u1 < v0;
    }

    public Line copy() {
        return this;
    }

    @Override
    public String toString() {
        return "<" + a + ", " + b + ">";
    }
}
