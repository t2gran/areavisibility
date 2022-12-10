package view;

import geometri.Area;
import geometri.Line;
import geometri.VisibilityLines;
import geometri.vl.DeluneyVLStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Simulator extends JPanel {

    public static final long STEP_DELAY = 1000L;
    public static final long STEP_DELAY_FF = 100L;
    public static final long STEP_SKIP = 1L;


    private final Area sample;
    private final String id;
    private final VisibilityLines vl;
    private final List<Line> currentLines = new ArrayList<>();
    private final Canvas2D canvas = new Canvas2D();
    private CtrlState ctrl = CtrlState.PLAY;
    private Thread mainThread;

    public Simulator(String id, Area sample) {
        this.id = id;
        this.sample = sample;
        //var vlStrategy = new AllVLStrategy();
        var vlStrategy = new DeluneyVLStrategy();
        this.vl = new VisibilityLines(sample, vlStrategy);
    }


    public void run(boolean pauseWhenDone) {
        ctrl = CtrlState.PLAY;
        mainThread =  Thread.currentThread();
        addMouseListener(new MouseAdapter() {
          @Override
          public void mouseReleased(MouseEvent e) {
            log(e.getX(), e.getY());
            setControl(canvas.controls.click(e.getX(), e.getY()));
            super.mouseReleased(e);
          }
        });

        display();
        step();

        vl.generate(this::updateDisplay);

        // Clear current lines
        for (int i = 0; i < currentLines.size(); i++) {
            updateDisplay(null);
        }
        if(pauseWhenDone) {
            setControl(CtrlState.PAUSE);
            updateDisplay(null);
        }
    }

    private void setControl(CtrlState ctrl) {
        if(ctrl == null || this.ctrl == ctrl) { return; }
        boolean interrupt = this.ctrl == CtrlState.PAUSE;
        System.out.println(ctrl);
        this.ctrl = ctrl;

        if(interrupt) { mainThread.interrupt(); }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(g.getFont().deriveFont(20.0f));
        g.setColor(new Color(0xD0E0F0));
        g.drawString(id, 20, 30);
        canvas.draw(g, sample, currentLines);
        // canvas.drawCurrentLines(g, currentLines);
        //canvas.drawVisibilityLines(g, vl);
        //canvas.drawPolygon(g, sample);
    }

    private void display() {
        var frame = new JFrame("Visibility ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = Canvas2D.winSize();
        this.setSize(size);
        this.setPreferredSize(size);
        this.setBackground(Canvas2D.BG_COLOR);
        canvas.setBoundingBox(sample.boarder().points());
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    private void updateDisplay(Line line) {
        currentLines.add(0, line);
        if(currentLines.size() > canvas.clSize()) {
            currentLines.remove(currentLines.size()-1);
        }
        repaint();
        step();
    }

    private void log(int viewX, int viewY) {
        System.out.printf("    v2(%d, %4d),%n", canvas.x(viewX), canvas.y(viewY));
    }

    private void step() {
        try {
            Thread.sleep(stepSleepTime());
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted: "  + Thread.interrupted());
        }
    }

    private long stepSleepTime() {
        return switch (ctrl) {
            case PAUSE -> Integer.MAX_VALUE;
            case FAST_FORWARD -> STEP_DELAY_FF;
            case SUPER_FAST_FORWARD -> STEP_SKIP;
            case PLAY -> STEP_DELAY;
        };
    }
}
