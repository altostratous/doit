package models;

import controllers.Configuration;
import views.Drawable;

import javax.xml.xpath.XPathExpressionException;
import java.awt.*;

/**
 * Created by HP PC on 5/20/2016.
 */
public class Piece implements Drawable{
    private double x, y, radius, subradius, rotation;

    public Piece(Configuration configuration) throws XPathExpressionException {
        x = configuration.getDouble("config/game/piece/x");
        y = configuration.getDouble("config/game/piece/y");
        radius = configuration.getDouble("config/game/piece/radius");
        subradius = configuration.getDouble("config/game/piece/subradius");
        rotation = 0;
    }

    @Override
    public void draw(Graphics2D graphics2D, Point location) {
        graphics2D.setColor(Color.BLUE);
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
}
