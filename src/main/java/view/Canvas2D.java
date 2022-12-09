package view;

import geometri.Area;
import geometri.Edge;
import geometri.Line;
import geometri.Node;
import geometri.Point;
import geometri.Polygon;

import java.awt.*;
import java.util.List;

public class Canvas2D {

    public static final Color BG_COLOR = new Color(0x404040);
    public static final Color BG_POLYGON = new Color(0xB0B0B0);
    public static final Color HOLES_COLOR = new Color(0x606060);
    private static final Color MAIN_LINE_COLOR = new Color(0x40A0FF);
    private static final Color VISIBILITY_LINE_COLOR = new Color(0xFF0080);
    private static final Color NODE_COLOR_A = new Color(0x20e040);
    private static final Color NODE_COLOR_B = new Color(0x20a040);
    private static final Color[] CURRENT_LINE_COLORS = {
            new Color(0xFF00FFFF, true),
            new Color(0x8000FFFF, true),
            new Color(0x6000FFFF, true),
            new Color(0x4000FFFF, true),
            new Color(0x2000FFFF, true),
            new Color(0x1000FFFF, true),
    };

    private static final int POINT_SIZE = 8;
    private static final int POINT_RADIUS = POINT_SIZE / 2;
    private static final int MARGIN = 100;
    public static final int WIN_WIDTH = 600;
    public static final int WIN_HEIGHT = 400;

    private static final int VISIBLE_WIN_WIDTH = WIN_WIDTH - 2 * MARGIN;
    private static final int VISIBLE_WIN_HEIGHT = WIN_HEIGHT - 2 * MARGIN;

    public final Controls controls  = new Controls(WIN_WIDTH/2, WIN_HEIGHT-32);
    private double scaleFactor;
    private int x0;
    private int y1;

    public static Dimension winSize() {
        return new Dimension(WIN_WIDTH, WIN_HEIGHT);
    }

    public int clSize() {
        return CURRENT_LINE_COLORS.length;
    }

    private void drawPoint(Graphics g, Point point) {
        g.fillOval(x(point)-POINT_RADIUS, y(point)-POINT_RADIUS, POINT_SIZE, POINT_SIZE);
    }

    public void drawLine(Graphics g, Line<?> line) {
        drawLine(g, line.a, line.b);
    }

    private void drawLine(Graphics g, Point a, Point b) {
        g.drawLine(x(a), y(a), x(b), y(b));
    }

    public void draw(Graphics g, Area area, List<Line<Point>> currentLines) {
        drawAreaBackground(g, area);

        for (Edge edge : area.edges()) {
            g.setColor(edge.fixed() ? MAIN_LINE_COLOR : VISIBILITY_LINE_COLOR);
            drawLine(g, edge);
        }

        drawCurrentLines(g, currentLines);

        for (Node node : area.nodes()) {
            g.setColor(node.fixed() ? NODE_COLOR_A : NODE_COLOR_B);
            drawPoint(g, node);
        }
        controls.draw(g);
    }

    public void drawAreaBackground(Graphics g, Area area) {
        g.setColor(BG_POLYGON);
        fillPolygon(g, area.boarder());
        g.setColor(HOLES_COLOR);
        area.holes().forEach(it -> fillPolygon(g, it));
    }

    private void drawCurrentLines(Graphics g, List<Line<Point>> currentLines) {
        if(currentLines == null) { return; }
        for (int  i=0; i< currentLines.size(); ++i) {
            var line = currentLines.get(i);
            if(line != null) {
                g.setColor(CURRENT_LINE_COLORS[i]);
                drawLine(g, line.a, line.b);
            }
        }
    }

    private void fillPolygon(Graphics g, Polygon<?,?> polygon) {
        g.fillPolygon(polygon.coordinates(this::x), polygon.coordinates(this::y), polygon.size());
    }

    private int x(Point v) {
        return x0 + (int)(v.x * scaleFactor);
    }

    private int y(Point v) {
        return y1 - (int)(v.y * scaleFactor);
    }

    public int x(int viewX) {
        return (int)((viewX -  x0) / scaleFactor);
    }

    public int y(int viewY) {
        return (int)((y1 - viewY) / scaleFactor);
    }


    public void setBoundingBox(List<? extends Point> points) {
        final double[] max = new double[2];

        for (Point p : points) {
            max[0] = Math.max(p.x, max[0]);
            max[1] = Math.max(p.y, max[1]);
        }

        double xScale = VISIBLE_WIN_WIDTH / max[0];
        double yScale = VISIBLE_WIN_HEIGHT / max[1];

        if(yScale < xScale) {
            scaleFactor = yScale;
            x0 = (int)((WIN_WIDTH - (max[0] * scaleFactor))/2.0);
            y1 = WIN_HEIGHT - MARGIN;
        }
        else {
            scaleFactor = xScale;
            x0 = MARGIN;
            y1 = (int)((WIN_HEIGHT + max[1] * scaleFactor) / 2.0);
        }
    }

}
