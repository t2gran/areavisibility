package geometri;

public class Angle {

    static double theta(double dx, double dy) {
        return Math.atan2(dy, dx);
    }

    static double thetaInv(double angle) {
        return angle  + (angle > 0 ? -Math.PI : Math.PI);
    }


    /**
     * The alfa value os a simple representation of an angle. The alfa value is a real number
     * between zero(0) and eight(8). Here are some example values:
     * <pre>
     *  Alfa | Degree
     *  -----|-------
     *  0.0  |   0°
     *  1.0  |  45°
     *  2.0  |  90°
     *  3.0  | 135°
     *  4.0  | 180°
     *  5.0  | 225°
     *  6.0  | 270°
     *  7.0  | 315°
     *  8.0  | 360°
     * </pre>
     * The {@code alfa} value is NOT uniform, it  does not increase in even steps - it can only be
     * used to compare two angles. The advantage of the angle is that only one division is used to
     * compute it, no sin(), cos() or tan() function is needed.
     * <p>
     * The 0 point is -45, not 0° - this is again an optimization to avoid one comparison.
     */
    static double alfaAngle(double dx, double dy) {
        if(dy >= 0.0) {
            if(dx >= 0.0) {
                return dx > dy ? dy/dx : 2 - dx/dy;
            }
            else {
                return -dx < dy ? 2 - dx/dy : 4 + dy/dx;
            }
        }
        else {
            if(dx < 0.0) {
                return dx < dy ? 4 + dy/dx : 6 - dx/dy;
            }
            else {
                return dx < -dy ? 6 - dx/dy : 8 + dy/dx;
            }
        }
    }

    static double alfaInverse(double angle) {
        return angle < 4.0 ? angle + 4.0  : angle -4.0;
    }

    static boolean between(double a, double a0, double a1) {
        if(a0 >  a1) { return a < a0 && a > a1; }
        else { return a < a0 || a > a1; }
    }

    static boolean notBetween(double a, double a0, double a1) {
        if(a0 > a1) { return a >= a0 || a <= a1; }
        else { return a >= a0 && a <= a1; }
    }
}
