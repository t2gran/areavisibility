package geometri;

import java.util.Optional;


/**
 * Two lines intersect if they share a common point. There is a special case
 * if one of the lines start or end at a point located on the other line.
 */
class LineIntersectionPointCalc {
  private double t;
  private final Line lineA;
  private final boolean intersect;

  public LineIntersectionPointCalc(Line lineA, Line lineB) {
    this.lineA = lineA;
    var p1 = lineA.a;
    var p2 = lineA.b;
    var p3 = lineB.a;
    var p4 = lineB.b;

    // If the lines share a node/point then, they do not intersect
    if(p1.equals(p3) || p1.equals(p4) || p2.equals(p3) || p2.equals(p4)) {
      intersect = false;
      return;
    }

    double x12 = p1.x - p2.x;
    double x13 = p1.x - p3.x;
    double x34 = p3.x - p4.x;

    double y12 = p1.y - p2.y;
    double y13 = p1.y - p3.y;
    double y34 = p3.y - p4.y;

    double d = x12 * y34 - y12 * x34;

    // The lines are parallel
    if (Math.abs(d) < 0.001) {
      intersect = false;
      return;
    }

    t = (x13 * y34 - y13 * x34) / d;
    var u = (x13 * y12 - y13 * x12) / d;

    intersect = isIntersecting(t) && isIntersecting(u);
  }

  boolean intersect() {
    return intersect;
  }

  Optional<Point> point() {
    return intersect
      ? Optional.of(new Point(lineA.a.x + t * lineA.dx(), lineA.a.y + (t * lineA.dy())))
      : Optional.empty();
  }

  private static boolean isIntersecting(double v) {
    return v > -0.0001 && v < 1.0001;
  }

}
