package geometri;

import org.junit.jupiter.api.Test;

import static geometri.V2.v2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LineTest {

    @Test
    void side() {
        var line = new Line(v2(1,1), v2(2,2));
        assertEquals(Bound.INSIDE, line.side(v2(1,2)));
        assertEquals(Bound.OUTSIDE, line.side(v2(2,1)));
        assertEquals(Bound.ON, line.side(v2(2,2)));
    }

    @Test
    void intersect() {
        // Lines intersect in the "middle"
        var l1 = new Line<>(v2(1,1), v2(2,2));
        var l2 = new Line<>(v2(1,2), v2(2,1));
        //assertTrue(l1.intersect(l2));
        //assertTrue(l2.intersect(l1));

        // Lines witch sheer one node, should not intersect
        l1 = new Line<>(v2(1,1), v2(2,2));
        l2 = new Line<>(v2(1,1), v2(2,1));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));

        // Lines where one end is in the "middle" of another line do intersect
        l1 = new Line<>(v2(1,1), v2(3,3));
        l2 = new Line<>(v2(2,2), v2(3,2));
        assertTrue(l1.intersect(l2));
        assertTrue(l2.intersect(l1));

        // Lines where one end is lies on the extension of the other do not intersect
        l1 = new Line<>(v2(2,2), v2(3,3));
        l2 = new Line<>(v2(1,1), v2(3,2));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));

        // When the
        l1 = new Line<>(v2(336.6, 200.0), v2(350.0, 250.0));
        l2 = new Line<>(v2(336.6, 200.0), v2(500.0, 0.0));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));
    }
}