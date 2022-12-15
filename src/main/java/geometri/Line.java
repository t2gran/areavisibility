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
    var test = (p.x - a.x) * (b.y - a.y) - (p.y - a.y) * (b.x - a.x);

    if (test < -0.001) {
      return Bound.INSIDE;
    }
    if (test > 0.001) {
      return Bound.OUTSIDE;
    }
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
   * A line is flat if it is closer to be a horizontal line than a vertical line. A
   * flat line will always have a {@code abs(dx) > abs(dy)}.
   */
  public boolean isFlat() {
    return Angle.isFlat(angleAlfaCCV);
  }

  /**
   * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line_segment
   */
  public boolean intersect(Line o) {
    return new IntersectionPoint(this, o).intersect();
  }

  public IntersectionPoint intersectionPoint(Line other) {
    return new IntersectionPoint(this, other);
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

  /**
   * This method test if the point is a possible intersection point. If the line
   * is {@link #isFlat()} the x-coordinates of the line and point is compared and true
   * is returned if the point is between the min and max x coordinate for the line.
   * If the line is NOT flat, then the y coordinates are used. Using x or y coordinate
   * is a trick to avoid dx/dy close to zero.
   * <p>
   * This is a lightweight test to see if the point is potentially inside the bounding
   * box of the line, only one dimension x or y is checked.
   */
  public boolean hasPossibleIntersectionPoint(Point p) {
    double v0, v1, v;
    if(isFlat()) {
      v = p.x;
      v0 = a.x;
      v1 = b.x;
    }
    else {
      v = p.y;
      v0 = a.y;
      v1 = b.y;
    }
    return  (v0 < v1) ? (v > v0 && v < v1) : (v > v1 && v < v0);
  }

  public static Comparator<Line> compareLength() {
    return (o1, o2) -> (int) Math.round((o1.lengthSqrt - o2.lengthSqrt) * 100d);
  }

  public Line copy() {
    return this;
  }

  @Override
  public String toString() {
    return "<" + a + ", " + b + ">";
  }
}
