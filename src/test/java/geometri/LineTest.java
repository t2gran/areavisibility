package geometri;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static geometri.Point.p;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LineTest {

    @Test
    void side() {
        var line = new Line(p(1,1), p(2,2));
        assertEquals(Bound.INSIDE, line.side(p(1,2)));
        assertEquals(Bound.OUTSIDE, line.side(p(2,1)));
        assertEquals(Bound.ON, line.side(p(2,2)));
    }

    @Test
    void intersect() {
        Line l1, l2;
        // Lines intersect in the "middle"
        l1 = new Line(p(1,1), p(2,2));
        l2 = new Line(p(1,2), p(2,1));
        assertTrue(l1.intersect(l2));
        assertTrue(l2.intersect(l1));

        // Lines intersect in at same points as above,
        // but lines are extended
        l1 = new Line(p(-4, -4), p(3, 3));
        l2 = new Line(p(0, 3), p(4,-1));
        assertTrue(l1.intersect(l2));
        assertTrue(l2.intersect(l1));


        // Lines witch sheer one node, should not intersect
        l1 = new Line(p(1,1), p(2,2));
        l2 = new Line(p(1,1), p(2,1));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));

        // Lines where one end is in the "middle" of another line do intersect
        l1 = new Line(p(1,1), p(3,3));
        l2 = new Line(p(2,2), p(3,2));
        assertTrue(l1.intersect(l2));
        assertTrue(l2.intersect(l1));

        // Lines where one end is lies on the extension of the other do not intersect
        l1 = new Line(p(2,2), p(3,3));
        l2 = new Line(p(1,1), p(3,2));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));

        // When the
        l1 = new Line(p(336.6, 200.0), p(350.0, 250.0));
        l2 = new Line(p(336.6, 200.0), p(500.0, 0.0));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));
    }
}