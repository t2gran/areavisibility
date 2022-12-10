package view;

import java.awt.*;
import java.util.function.Consumer;

import static view.CtrlState.FAST_FORWARD;
import static view.CtrlState.PAUSE;
import static view.CtrlState.PLAY;
import static view.CtrlState.SUPER_FAST_FORWARD;

public class Controls {
    private static final Color BOX_COLOR = new Color(0x4080E0);
    private static final Color BOX_BG_COLOR = new Color(0x101060);
    private static final Color CONTROL_COLOR = new Color(0x80C0FF);
    private static final Color CONTROL_BG_COLOR = new Color(0xA0A0A0);
    private static final Color SELECTED_BG_COLOR = new Color(0xFFFFFF);
    private static final int HEIGHT = 16;
    private static final int ARROW_WIDTH = 8;
    private static final int PIPE_WIDTH = 4;
    private static final int SPACE = 70;
    private static final int MARGIN_Y = 8;

    private final int x0;
    private final int y0;
    private CtrlState state = CtrlState.PLAY;

    private Consumer<CtrlState> stateChangedSubscriber = null;

    Controls(int x0, int y0) {
        this.x0 = x0 - 2*SPACE;
        this.y0 = y0;
    }

    public CtrlState state() {
        return state;
    }

    public void click(int x, int y) {
        if(y < y0 || y > y0+HEIGHT ) { return; }
        if(x < x0 || x > x0 + 4 * SPACE) { return; }

        if(x < x0 + SPACE ) { updateState(PAUSE); }
        else if(x < x0 + 2 * SPACE ) { updateState(CtrlState.PLAY); }
        else if(x < x0 + 3 * SPACE ) { updateState(CtrlState.FAST_FORWARD); }
        else if(x < x0 + 4 * SPACE ) { updateState(CtrlState.SUPER_FAST_FORWARD); }
    }

    public void updateState(CtrlState state) {
        if(this.state != state) {
            this.state = state;
            if(stateChangedSubscriber != null) {
                stateChangedSubscriber.accept(this.state);
            }
        }
    }

    public void subscribe(Consumer<CtrlState> stateChangedSubscriber) {
        this.stateChangedSubscriber  =stateChangedSubscriber;
    }

    void draw(Graphics g) {
        drawBox(g);
        // Pause
        int x = x0 + SPACE/2 + 1;
        drawPipe(g, x - 3*PIPE_WIDTH/2, PAUSE);
        drawPipe(g, x + PIPE_WIDTH/2, PAUSE);
        // Play
        x += SPACE;
        drawArrow(g, x - ARROW_WIDTH/2, PLAY);
        // Fast Forward
        x += SPACE;
        drawArrow(g, x - ARROW_WIDTH, FAST_FORWARD);
        drawArrow(g, x, FAST_FORWARD);
        // Super Fast Forward
        x += SPACE;
        drawArrow(g, x - 3*ARROW_WIDTH/2, SUPER_FAST_FORWARD);
        drawArrow(g, x - ARROW_WIDTH/2, SUPER_FAST_FORWARD);
        drawArrow(g, x + ARROW_WIDTH/2, SUPER_FAST_FORWARD);
    }

    private void drawArrow(Graphics g, int x, CtrlState state) {
        int x1 = x + ARROW_WIDTH;

        int[] xs = { x, x1, x };
        int[] ys = { y0, y0 + HEIGHT/2, y0 + HEIGHT};

        setCtrlBgColor(g, state);
        g.fillPolygon(xs, ys, 3);
        g.setColor(CONTROL_COLOR);
        g.drawPolygon(xs, ys, 3);
    }

    private void drawPipe(Graphics g, int x, CtrlState state) {
        int y = y0;

        setCtrlBgColor(g, state);
        g.fillRect(x, y, PIPE_WIDTH, HEIGHT);
        g.setColor(CONTROL_COLOR);
        g.drawRect(x, y, PIPE_WIDTH, HEIGHT);
    }

    private void setCtrlBgColor(Graphics g, CtrlState state) {
        g.setColor(this.state == state ? SELECTED_BG_COLOR : CONTROL_BG_COLOR);
    }

    private void drawBox(Graphics g)  {
        int w = 4 * SPACE;
        int h = HEIGHT + 2 * MARGIN_Y;
        int x  = x0;
        int y  = y0 - MARGIN_Y;

        g.setColor(BOX_BG_COLOR);
        g.fillRect(x, y, w, h);
        g.setColor(BOX_COLOR);
        g.drawRect(x, y, w, h);
        for(int i=x+SPACE; i < x+w; i+=SPACE) {
            g.drawLine(i, y, i, y + h);
        }
    }
}
