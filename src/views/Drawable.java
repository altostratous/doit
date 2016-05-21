package views;

import java.awt.*;

/**
 * Created by HP PC on 5/20/2016.
 */
public interface Drawable {
    void draw(Graphics2D graphics2D, Point location);

    void draw(Graphics2D graphics2D);
}
