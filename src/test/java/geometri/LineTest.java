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
    void doNotIntersect() {
        // Lines witch sheer one node, should not intersect
        var l1 = new Line(p(0, 0), p(60, 15));
        var l2 = new Line(p(45, 30), p(38, 20));
        assertEquals(Optional.empty(), l1.intersectionPoint(l2));
        assertEquals(Optional.empty(), l2.intersectionPoint(l1));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));
    }

    @Test
    void intersectInTheMiddle() {
        var l1 = new Line(p(1, 1), p(2, 2));
        var l2 = new Line(p(1, 2), p(2, 1));
        assertEquals(Optional.of(p(1.5, 1.5)), l1.intersectionPoint(l2));
        assertEquals(Optional.of(p(1.5, 1.5)), l2.intersectionPoint(l1));
        assertTrue(l1.intersect(l2));
        assertTrue(l2.intersect(l1));
    }

    @Test
    void intersectLongLinesWithMixesCoordinates() {
        // Lines intersect in at same points as above,
        // but lines are extended
        var l1 = new Line(p(-3, -2), p(3, 4));
        var l2 = new Line(p(0, 4), p(4, 0));
        assertEquals(Optional.of(p(1.5, 2.5)), l1.intersectionPoint(l2));
        assertEquals(Optional.of(p(1.5, 2.5)), l2.intersectionPoint(l1));
    }

    @Test
    void linesSharingOnePointDoesNotIntersect() {
        var l1 = new Line(p(1, 1), p(2, 2));
        var l2 = new Line(p(1, 1), p(2, 1));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));
    }
    @Test
    void linesWithOneEndOnTheOtherLineDoIntersect() {
        // (2, 2) is in the middle of line l1
        var l1 = new Line(p(1, 1), p(3, 3));
        var l2 = new Line(p(2, 2), p(3, 2));
        assertEquals(Optional.of(p(2,2)), l1.intersectionPoint(l2));
        assertEquals(Optional.of(p(2,2)), l2.intersectionPoint(l1));
    }
    @Test
    void linesWhereOneEndIsOnTheExtensionOfTheOtherDoNotIntersect() {
        // Lines where one end is lies on the extension of the other do not intersect
        var l1 = new Line(p(2, 2), p(3, 3));
        var l2 = new Line(p(1, 1), p(3, 2));
        assertFalse(l1.intersect(l2));
        assertFalse(l2.intersect(l1));
    }
}