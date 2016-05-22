package views;

import controllers.Configuration;
import controllers.Game;

import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;

/**
 * Created by HP PC on 5/22/2016.
 */
public class ScorePage extends FirstPage {
    Configuration configuration;
    Game game;

    public ScorePage(Configuration configuration, Game game, MainWindow mainWindow) throws XPathExpressionException {
        super(configuration, mainWindow);
        this.configuration = configuration;
        this.game = game;
    }
}
