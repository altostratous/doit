import controllers.Configuration;
import views.ErrorWindow;
import views.MainWindow;

import javax.sound.sampled.*;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.io.IOException;

/**
 * Created by HP PC on 5/18/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Configuration configuration;
        try {
            configuration = new Configuration(Main.class.getResourceAsStream("resources/config.xml"));
        } catch (Exception e) {
            ErrorWindow.viewError(e, "An error occurred while getting configurations. details:");
            return;
        }

        MainWindow mainWindow;
        try {
            mainWindow = new MainWindow(configuration);
            mainWindow.setSize(
                    configuration.getInteger("/config/settings/window/size/width"),
                    configuration.getInteger("/config/settings/window/size/height"));

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            mainWindow.setLocation(dim.width/2- mainWindow.getSize().width/2, dim.height/2- mainWindow.getSize().height/2);

            mainWindow.setTitle(configuration.getString("/config/settings/window/title"));
        } catch (XPathExpressionException e) {
            ErrorWindow.viewError(e);
            return;
        }
        mainWindow.setVisible(true);
    }

}
