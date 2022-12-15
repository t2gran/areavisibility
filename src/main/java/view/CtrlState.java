package view;

public enum CtrlState {
    PAUSE(10_000_000),
    PLAY(700),
    FAST_FORWARD(100),
    SUPER_FAST_FORWARD(2);

    private final int delay;

    CtrlState(int delay) {
        this.delay = delay;
    }

    public int delay() {
        return delay;
    }
}
