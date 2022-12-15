package geometri;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AngleTest {
    private static final double EPSILON  = 0.0015;

    @Test
    void testAlfaAngle() {
        assertEquals(0.0, Angle.alfaAngle(1.0, 0.0), EPSILON);
        assertEquals(1.0, Angle.alfaAngle(1.0, .999), EPSILON);
        assertEquals(1.0, Angle.alfaAngle(.999, 1.0), EPSILON);
        assertEquals(2.0, Angle.alfaAngle(0.001, 1.0), EPSILON);
        assertEquals(2.0, Angle.alfaAngle(-0.001, 1.0), EPSILON);
        assertEquals(3.0, Angle.alfaAngle(-.999, 1.0), EPSILON);
        assertEquals(3.0, Angle.alfaAngle(-1.0, .999), EPSILON);
        assertEquals(4.0, Angle.alfaAngle(-1.0, 0.001), EPSILON);
        assertEquals(4.0, Angle.alfaAngle(-1.0, -0.001), EPSILON);
        assertEquals(5.0, Angle.alfaAngle(-1.0, -.999), EPSILON);
        assertEquals(5.0, Angle.alfaAngle(-0.999, -1.0), EPSILON);
        assertEquals(6.0, Angle.alfaAngle(-0.001, -1.0), EPSILON);
        assertEquals(6.0, Angle.alfaAngle(0.001, -1.0), EPSILON);
        assertEquals(7.0, Angle.alfaAngle(.999, -1.0), EPSILON);
        assertEquals(7.0, Angle.alfaAngle(1.0, -.999), EPSILON);
        assertEquals(8.0, Angle.alfaAngle(1.0, -0.001), EPSILON);
    }
}