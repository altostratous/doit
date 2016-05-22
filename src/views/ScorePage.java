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
    long highScore = 0;
    JLabel hScore;
    JLabel yourScore;
    public ScorePage(Configuration configuration, Game game, MainWindow mainWindow) throws XPathExpressionException {
        super(configuration, mainWindow);
        this.configuration = configuration;

        hScore = new JLabel("Highest Score: " + highScore);
        yourScore = new JLabel("Your Score: " + game.getScore());

        update(game);

        add(hScore);
        add(yourScore);
    }

    public void update(Game game)
    {
        this.game = game;
        if (game.getScore() > highScore)
            highScore = game.getScore();

        hScore.setText("Highest Score: " + highScore);
        yourScore.setText("Your Score: " + game.getScore());
    }
}
