package visibilityline;

import api.AreaAlgorithm;
import geometri.Area;

public class MergeAndDeluneyStrategy {

  public static final String ID = "merge&Deluney";

  public static AreaAlgorithm of() {
    return new CompositeAreaAlgorithm(ID, MergeVLStrategy.of(), DeluneyVLStrategy.of(), Area::removePathsButKeepIntersectionPoints);
  }
}
