package view;

import geometri.AddVLStrategy;
import geometri.Area;
import geometri.Line;
import geometri.VisibilityLines;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Simulator extends JPanel {

    public static final long STEP_DELAY = 1000L;
    public static final long STEP_DELAY_FF = 100L;
    public static final long STEP_SKIP = 1L;
    private final String name;
    private final Area originalSample;
    private final AddVLStrategy addVlStrategy;
    private final List<Line> currentLines = Collections.synchronizedList(new ArrayList<>());
    private final Canvas2D canvas = new Canvas2D();
    private Area currentSample;
    private List<? extends Line> vlLines;
    private Thread mainThread;
    private boolean paused = false;

    public Simulator(Area sample, AddVLStrategy addVlStrategy) {
        this.name = "Visibility " + sample.name() + " " + addVlStrategy.name();
        this.addVlStrategy = addVlStrategy;
        this.originalSample = sample;
    }

    public void run() {
        setup();
        //noinspection InfiniteLoopStatement
        while(true) {
            currentSample = originalSample.copy();
            currentLines.clear();
            var vl = new VisibilityLines(currentSample, addVlStrategy);
            vlLines = vl.visibilityLines();

            step();
            vl.generate(this::updateDisplay);

            clearCurrentLines();
            pauseSimulation();
            step();
        }
    }

    private void setup() {
        mainThread = Thread.currentThread();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                log(e.getX(), e.getY());
                canvas.controls.click(e.getX(), e.getY());
                super.mouseReleased(e);
            }
        });
        var frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = Canvas2D.winSize();
        this.setSize(size);
        this.setPreferredSize(size);
        this.setBackground(Canvas2D.BG_COLOR);
        canvas.setBoundingBox(originalSample.boarder().points());
        canvas.controls.subscribe(this::ctrlChangedNotification);
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(g.getFont().deriveFont(20.0f));
        g.setColor(new Color(0xD0E0F0));
        g.drawString(name, 20, 30);

        if(simulationDone()) {
            canvas.drawVLLines(g, currentSample, List.of());
        }
        else {
            canvas.drawVLAndCurrentLines(g, currentSample, vlLines, currentLines);
        }
    }

    private void clearCurrentLines() {
        int size = canvas.clSize() + 1;
        for (var i = 0; i < size; i++) {
            updateDisplay(null);
        }
        repaint();
    }

    private void updateDisplay(Line currentLine) {
        currentLines.add(0, currentLine);
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
        return switch (ctrlSate()) {
            case PAUSE -> Integer.MAX_VALUE;
            case FAST_FORWARD -> STEP_DELAY_FF;
            case SUPER_FAST_FORWARD -> STEP_SKIP;
            case PLAY -> STEP_DELAY;
        };
    }

    private boolean simulationDone() {
        return currentLines.stream().allMatch(Objects::isNull);
    }

    private CtrlState ctrlSate() {
        return canvas.controls.state();
    }
    private void pauseSimulation() {
        canvas.controls.updateState(CtrlState.PAUSE);
    }

    private void ctrlChangedNotification(CtrlState ctrl) {
        if(paused) {
            mainThread.interrupt();
        }
        this.paused = (ctrl == CtrlState.PAUSE);
        repaint();
    }
}
