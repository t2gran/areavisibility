package api;

import geometri.Line;


/**
 * Callback to instruct the animation.
 */
public interface Animation {
  void step(Line line);
  void startSection();
  void endSection();
}
