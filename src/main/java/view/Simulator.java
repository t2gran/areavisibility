package view;

import api.Animation;
import api.AreaAlgorithm;
import geometri.Area;
import geometri.Line;
import geometri.NodeFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Simulator extends JPanel {
    private final String name;
    private final Area originalArea;
    private final AreaAlgorithm areaAlgorithm;
    private final List<Line> currentLines = new CopyOnWriteArrayList<>();
    private final Canvas2D canvas = new Canvas2D();
    private Area currentArea;
    private Thread mainThread;
    private boolean paused = false;

    public Simulator(Area sample, AreaAlgorithm areaAlgorithm) {
        this.name = "Visibility " + sample.name() + " " + areaAlgorithm.name();
        this.areaAlgorithm = areaAlgorithm;
        this.originalArea = sample;
    }

    public void run() {
        setup();
        //noinspection InfiniteLoopStatement
        while(true) {
            var animation = animation();
            currentArea = originalArea.copy(new NodeFactory());
            currentLines.clear();
            animation.startSection();
            areaAlgorithm.updateArea(currentArea, animation);
            animation.endSection();
            currentArea.removeCandidates();
            pauseSimulation();
        }
    }

    private void setup() {
        mainThread = Thread.currentThread();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(!canvas.controls.click(e.getX(), e.getY())) {
                    log(e.getX(), e.getY());
                }
                super.mouseReleased(e);
            }
        });
        var frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = Canvas2D.winSize();
        this.setSize(size);
        this.setPreferredSize(size);
        this.setBackground(Canvas2D.BG_COLOR);
        canvas.setBoundingBox(originalArea.boarder().points());
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
            canvas.drawVLLines(g, currentArea);
        }
        else {
            canvas.drawVLAndCurrentLines(g, currentArea, currentLines);
        }
    }

    private void clearCurrentLines() {
        int size = canvas.clSize() + 1;
        for (var i = 0; i < size+1; i++) {
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

    Animation animation() {
        return new Animation() {
            @Override public void step(Line line) { updateDisplay(line); }
            @Override public void startSection() {
                repaint();
                Simulator.this.step();
            }
            @Override public void endSection() {
                clearCurrentLines();
                repaint();
                pauseSimulation();
            }
        };
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
        return ctrlSate().delay();
    }

    private boolean simulationDone() {
        return currentLines.stream().allMatch(Objects::isNull);
    }

    private CtrlState ctrlSate() {
        return canvas.controls.state();
    }
    private void pauseSimulation() {
        canvas.controls.updateState(CtrlState.PAUSE);
        step();
    }

    private void ctrlChangedNotification(CtrlState ctrl) {
        if(paused) {
            mainThread.interrupt();
        }
        this.paused = (ctrl == CtrlState.PAUSE);
        repaint();
    }
}
