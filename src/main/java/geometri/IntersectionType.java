package geometri;


/**
 * This describes how a line intersect with another line, the options
 * are: BEGINNING, MIDDLE, END, NOT_INTERSECT.
 */
enum IntersectionType {
  /**
   * Intersection point is on the line, but not in the start or end of the line.
   */
  MIDDLE,
  /**
   * Intersection point is very close to the start point of the line
   */
  BEGINNING,
  /**
   * Intersection point is very close to the end point of the line
   */
  END,
  /**
   * Line does not have an intersection point
   */
  NOT_INTERSECT;

  boolean inBeginning() { return this == BEGINNING; }
  boolean inMiddle() { return this == MIDDLE; }
  boolean inEnd() { return this == END; }
  boolean anyPlace() { return this != NOT_INTERSECT; }
  boolean doesNotExist() { return this == NOT_INTERSECT; }
}
