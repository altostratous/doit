package models;

import controllers.Configuration;
import views.Drawable;

import javax.xml.xpath.XPathExpressionException;
import java.awt.*;

/**
 * Created by HP PC on 5/19/2016.
 */
public class State implements Drawable{
    Obstacle obstacle;
    Piece piece;
    private long score;

    public int getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(int angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    int angularVelocity;
    double speedCoefficient;

    public long getTime() {
        return time;
    }

    public void addTime(long dt)
    {
        time+= dt;
        score+= dt;
    }

    long time;
    long initTime;
    Configuration configuration;
    public State(Configuration configuration) throws XPathExpressionException {
        this.configuration = configuration;
        this.score = 0;
        piece = new Piece(configuration);
        obstacle = new Obstacle(configuration, this);
        speedCoefficient = 0.7;
        initTime = System.currentTimeMillis();
    }

    @Override
    public void draw(Graphics2D graphics2D, Point location) {
        piece.draw(graphics2D);
        obstacle.draw(graphics2D);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        draw(graphics2D, new Point(0, 0));
    }

    public Piece getPiece() {
        return piece;
    }

    public double getSpeedCoefficient() {
        return speedCoefficient;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void generateObstacle() throws XPathExpressionException {
        obstacle = new Obstacle(configuration, this);
    }

    public long getScore() {
        return score;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void addSpeed(double ds) {
        speedCoefficient += ds;

    }
}
