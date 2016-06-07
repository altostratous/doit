package models;

import controllers.Configuration;
import controllers.SoundClip;
import views.Drawable;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Random;

/**
 * Created by HP PC on 5/20/2016.
 */
public class Obstacle implements Drawable {
    Polygon polygon;
    ObstacleType obstacleType;
    double angularVelocity;
    Configuration configuration;
    Color color;
    State state;
    long lastChangeSide = 0;
    public Obstacle(Configuration configuration, State state) throws XPathExpressionException {
        obstacleType = randomType();
        this.state = state;
        this.configuration = configuration;
        polygon = createPolygon(obstacleType);
        if (obstacleType == ObstacleType.ROTATING_OBSTACLE || obstacleType == ObstacleType.ROTATING_POSITION_OBSTACLE || obstacleType == ObstacleType.ROTATING_SIDE_OBSTACLE)
            angularVelocity = configuration.getDouble("config/game/obstacle/angularvelocity");
        setColor();
    }

    private void setColor() {
        switch (obstacleType) {
            case ROTATING_OBSTACLE:
                color = Color.decode("#ff556f");
                break;
            case ROTATING_POSITION_OBSTACLE:
                color = Color.decode("#ffffff");
                break;
            case POSITION_OBSTACLE:
                color = Color.decode("#Ef555f");
                break;
            case BLINKING_SIDE_OBSTACLE:
                color = Color.decode("#5ff25f");
                break;
            case ROTATING_SIDE_OBSTACLE:
                color = Color.decode("#a6f183");
                break;
            case SIDE_OBSTACLE:
                color = Color.decode("#f68561");
                break;
        }
    }

    private Polygon createPolygon(ObstacleType obstacleType) throws XPathExpressionException {
        Rectangle rectangle = new Rectangle();
        Random random = new Random(System.currentTimeMillis());
        switch (obstacleType)
        {
            case ROTATING_OBSTACLE:
                rectangle = new Rectangle((int) (configuration.getInteger("config/game/obstacle/width")), configuration.getInteger("config/game/obstacle/height"));
                rectangle.setLocation((int) (configuration.getInteger("config/settings/window/size/width") / 2 - rectangle.getWidth() / 2),
                            (int) - rectangle.getHeight());
                break;
            case ROTATING_POSITION_OBSTACLE:
            case POSITION_OBSTACLE:
                rectangle = new Rectangle((int) ((configuration.getInteger("config/game/piece/radius") - configuration.getInteger("config/game/piece/subradius")) * 2 *
                    configuration.getDouble("config/game/obstacle/lesscoefficient")), configuration.getInteger("config/game/obstacle/height"));

                int left = random.nextInt(configuration.getInteger("config/settings/window/size/width") - configuration.getInteger("config/game/obstacle/width"));
                rectangle.setLocation(left, (int) -rectangle.getHeight());
                break;
            case BLINKING_SIDE_OBSTACLE:
            case ROTATING_SIDE_OBSTACLE:
            case SIDE_OBSTACLE:
                rectangle = new Rectangle((int) (configuration.getInteger("config/settings/window/size/width") / 2 *
                        configuration.getDouble("config/game/obstacle/morecoefficient")), configuration.getInteger("config/game/obstacle/height"));
                if (random.nextBoolean()) {
                    rectangle.setLocation(0, (int) -rectangle.getHeight());
                }
                else
                {
                    rectangle.setLocation((int) (configuration.getInteger("config/settings/window/size/width") - rectangle.getWidth()),
                            (int) - rectangle.getHeight());
                }
                break;
        }
        if (obstacleType == ObstacleType.ROTATING_SIDE_OBSTACLE)
        {
            rectangle.setSize((int)(configuration.getInteger("config/settings/window/size/width") / 2  * configuration.getDouble("config/game/obstacle/lesscoefficient")),(int)rectangle.getHeight());
            if (random.nextBoolean()) {
                rectangle.setLocation(0, (int) -rectangle.getHeight());
            }
            else
            {
                rectangle.setLocation((int) (configuration.getInteger("config/settings/window/size/width") - rectangle.getWidth()),
                        (int) - rectangle.getHeight());
            }
        }
        return createPolygon(rectangle);
    }

    private Polygon createPolygon(Rectangle rectangle) {
        int y = (int) Math.abs(rectangle.getY());
        if (rectangle.getY() < 0)
            y = -y;
        return new Polygon(
                new int[]{(int) rectangle.getX(), (int) (rectangle.getX() + rectangle.getWidth()), (int) (rectangle.getX() + rectangle.getWidth()), (int) rectangle.getX()},
                new int[]{y, y, (int) (y + rectangle.getHeight()), (int) (y + rectangle.getHeight())},
                4);
    }

    private ObstacleType randomType() {
        Random random = new Random(System.currentTimeMillis());
        int typeId = random.nextInt() % 6;
        typeId = Math.abs(typeId);
        switch (typeId)
        {
            case 0:
                return ObstacleType.BLINKING_SIDE_OBSTACLE;
            case 1:
                return ObstacleType.POSITION_OBSTACLE;
            case 2:
                return ObstacleType.ROTATING_OBSTACLE;
            case 3:
                return ObstacleType.SIDE_OBSTACLE;
            case 4:
                return ObstacleType.ROTATING_POSITION_OBSTACLE;
            case 5:
                return ObstacleType.ROTATING_SIDE_OBSTACLE;
        }
        return null;
    }

    @Override
    public void draw(Graphics2D graphics2D, Point location) {
        graphics2D.setColor(color);
        graphics2D.fillPolygon(polygon);
//        if (obstacleType == ObstacleType.BLINKING_SIDE_OBSTACLE) {
//            graphics2D.setColor(Color.RED);
//            graphics2D.drawPolygon(polygon);
//        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        draw(graphics2D, new Point(0, 0));
    }

    public void update(long dt) throws XPathExpressionException {
        transform(dt * configuration.getDouble("config/game/obstacle/speed") * state.getSpeedCoefficient(), dt * angularVelocity * state.getSpeedCoefficient());
    }

    private void transform(double dy, double rotation) {
        AffineTransform transform = new AffineTransform();
        transform.translate(polygon.getBounds().getX() + polygon.getBounds().getWidth() / 2,
                +polygon.getBounds().getY() + polygon.getBounds().getHeight() / 2);
        transform.rotate(rotation);
        transform.translate(-polygon.getBounds().getX() - polygon.getBounds().getWidth() / 2,
                -polygon.getBounds().getY() - polygon.getBounds().getHeight() / 2);
        Point2D[] point2Ds = getPolyPoints(polygon);
        for (Point2D point:
                point2Ds) {
            transform.transform(point, point);
        }
        polygon = createPolygon(point2Ds);
        polygon.translate(0, (int) dy);
    }

    private Polygon createPolygon(Point2D[] polyPoints) {
        Polygon polygon = new Polygon();
        for (Point2D point :
                polyPoints)
            polygon.addPoint((int)point.getX(), (int)point.getY());
        return polygon;
    }

    private Point2D[] getPolyPoints(Polygon polygon) {
        Point2D[] point2Ds = new Point2D[4];
        for (int i = 0; i < 4; i++) {
            point2Ds[i] = new Point(polygon.xpoints[i], polygon.ypoints[i]);
        }
        return point2Ds;
    }

    public boolean isAlive() throws XPathExpressionException {
        return polygon.getBounds().getY() < configuration.getInteger("config/settings/window/size/height");
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public ObstacleType getType() {
        return obstacleType;
    }

    public void changeSide() throws XPathExpressionException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (System.currentTimeMillis() - lastChangeSide > configuration.getInteger("config/game/obstacle/changesidelimit")) {
            for (int i = 0; i < polygon.npoints; i++) {
                polygon.xpoints[i] = configuration.getInteger("config/settings/window/size/width") - polygon.xpoints[i];
            }
            lastChangeSide = System.currentTimeMillis();
            new SoundClip(getClass().getResource("/resources/breeze.wav")).play();
        }
    }
}
