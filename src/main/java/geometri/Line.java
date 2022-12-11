package geometri;

import java.util.Comparator;
import java.util.Optional;

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
   * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line_segment
   */
  public boolean intersect(Line o) {
    return new LineIntersectionPointCalc(this, o).intersect();
  }

  public Optional<Point> intersectionPoint(Line other) {
    return new LineIntersectionPointCalc(this, other).point();
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
