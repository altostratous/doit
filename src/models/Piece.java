package models;

import controllers.Configuration;
import views.Drawable;

import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by HP PC on 5/20/2016.
 */
public class Piece implements Drawable{
    private double x, y, radius, subradius, rotation;
    private Color paint = Color.BLUE;
    public Piece(Configuration configuration) throws XPathExpressionException {
        x = configuration.getDouble("config/game/piece/x");
        y = configuration.getDouble("config/game/piece/y");
        radius = configuration.getDouble("config/game/piece/radius");
        subradius = configuration.getDouble("config/game/piece/subradius");
        rotation = 0;
    }

    @Override
    public void draw(Graphics2D graphics2D, Point location) {
        graphics2D.setColor(paint);
        graphics2D.drawOval((int)x + 1, (int)y + 1, (int)radius * 2 - 2, (int)radius * 2- 2);
        graphics2D.drawOval((int)x, (int)y, (int)radius * 2, (int)radius * 2);
        graphics2D.drawOval((int)x - 1, (int)y - 1, (int)radius * 2  + 2, (int)radius * 2+2);
        graphics2D.setColor(Color.GRAY);
        graphics2D.fillOval(getFirstSubX(), getFirstSubY(), (int)subradius * 2, (int)subradius * 2);
        graphics2D.fillOval(getSecondSubX(), getSecondSubY(), (int)subradius * 2, (int)subradius * 2);
    }

    private int getSecondSubY() {
        return (int)(getFirstSubY()  - 2 *(getFirstSubY() + subradius - getCenterY()));
    }

    private int getSecondSubX() {
        return (int)(getFirstSubX()  - 2 *(getFirstSubX() + subradius - getCenterX()));
    }

    private int getFirstSubY() {
        return (int)( getCenterY() - Math.sin(rotation) * radius - subradius);
    }

    private int getFirstSubX() {
        return (int)( getCenterX() - Math.cos(rotation) * radius - subradius);
    }

    private double getCenterX()
    {
        return x + radius;
    }

    private double getCenterY()
    {
        return y + radius;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        draw(graphics2D, new Point(0, 0));
    }

    public void rotate(double rotation)
    {
        this.rotation += rotation;
    }

    private boolean hitTest(Obstacle obstacle, double x, double y, double radius) {
        Polygon polygon = obstacle.getPolygon();
        Point2D nearest = getNearest(polygon, x, y);
        double px = nearest.getX();
        double py = nearest.getY();
        double dx = px - x;
        double dy = py - y;
        double dlength = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
//        dx *= subradius;
//        dy *= subradius;
//        dx /= dlength;
//        dy /= dlength;
//
//        return polygon.contains(x + dx, y + dy);
        return radius > dlength || polygon.contains(x, y);
    }

    private Point2D getNearest(Polygon polygon, double x, double y) {
        Point minPoint = new Point(polygon.xpoints[0], polygon.ypoints[0]);
        double minD = Double.MAX_VALUE;
        for (int i = 1; i < polygon.npoints + 1; i++) {
            double xi0 = polygon.xpoints[i - 1];
            double yi0 = polygon.ypoints[i - 1];
            double xi = polygon.xpoints[i % polygon.npoints];
            double yi = polygon.ypoints[i % polygon.npoints];
            double dx = xi - xi0;
            dx *= 0.01;
            double dy = yi - yi0;
            dy *= 0.01;
            for (; Math.abs(xi - xi0) > Math.abs(dx) || Math.abs(yi - yi0) > Math.abs(dy); xi0 += dx)
            {
                double d = Math.sqrt(Math.pow(xi0 - x, 2) + Math.pow(yi0 - y, 2));
                if (d < minD)
                {
                    minD = d;
                    minPoint = new Point((int)xi0, (int)yi0);
                }
                yi0 += dy;
            }
            int dk = 10;
        }
        return minPoint;
    }

    public boolean hitTest(Obstacle obstacle)
    {
        boolean test = hitTest(obstacle, getFirstSubX() + subradius, getFirstSubY() + subradius, subradius) || hitTest(obstacle, getSecondSubX() + subradius, getSecondSubY() + subradius, subradius);
        if (test)
            paint = Color.RED;
        return test;
    }

    public boolean approachTest(Obstacle obstacle) {
        boolean test = hitTest(obstacle, getFirstSubX() + subradius, getFirstSubY() + subradius, subradius * 3) || hitTest(obstacle, getSecondSubX() + subradius, getSecondSubY() + subradius, subradius * 3);
        if (test) {
            paint = Color.YELLOW;
        }
        else
        {
            paint = Color.BLUE;
        }
        return test;
    }
}
