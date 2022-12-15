package geometri;

/**
 * The status tell how permanent a geometry object is.
 * <p>
 * Currently, only used for edge. When an algorithm work it adds
 * new edges as ACCEPTED, then before the next algorithm is run
 * all edges is "downgraded" to candidates, and the new algorithm
 * should promote each edge back to ACCEPTED if the edge should
 * be kept.
 */
public enum Status {

  /** Permanent - should not be changes */
  FIXED,
  /** The "edge" is a candidate we will consider to keep.  */
  CANDIDATE,
  /** The "edge" is accepted by the current algorithm.  */
  ACCEPTED,
  /** The edge is not part of the graph.  */
  DISCONNECTED;

  boolean isFixed() { return this == FIXED; }
  boolean isCandidate() { return this == CANDIDATE; }
  boolean isAccepted() { return this == ACCEPTED; }
  boolean isDisconnected() { return this == DISCONNECTED; }
}
