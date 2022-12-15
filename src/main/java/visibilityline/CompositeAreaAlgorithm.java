package visibilityline;

import api.Animation;
import api.AreaAlgorithm;
import geometri.Area;

import java.util.function.Consumer;

/**
 * Combine two algorithms into one. This class do a few perform
 * a few things in between the two algorithms.
 */
public class CompositeAreaAlgorithm implements AreaAlgorithm {

  private final String id;
  private final AreaAlgorithm first;
  private final AreaAlgorithm second;
  private final Consumer<Area> prepareForSecond;

  public CompositeAreaAlgorithm(String id, AreaAlgorithm first, AreaAlgorithm second, Consumer<Area> prepareForSecond) {
    this.id = id;
    this.first = first;
    this.second = second;
    this.prepareForSecond = prepareForSecond;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public void updateArea(Area area, Animation animation) {
    System.out.println("Run: " + first.name());
    first.updateArea(area, animation);
    animation.endSection();

    prepareForSecond.accept(area);
    animation.startSection();
    System.out.println("And run: " + second.name());
    second.updateArea(area, animation);
  }
}
