package views;

import controllers.Configuration;
import controllers.Game;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.shape.Shape;

import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HP PC on 5/18/2016.
 */
public class GamePage extends JPanel {
    private Configuration configuration;
    private Game game;
    private JButton pause;
    private MainWindow mainWindow;
    public GamePage(Configuration configuration, Game game, MainWindow mainWindow) throws XPathExpressionException {
        this.mainWindow = mainWindow;
        this.game = game;
        setFocusable(true);
        this.configuration = configuration;
        this.addKeyListener(new KeyListener() {
                                @Override
                                public void keyTyped(KeyEvent e) {

                                }

                                @Override
                                public void keyPressed(KeyEvent e) {
                                    if (e.getKeyCode() == KeyEvent.VK_LEFT)
                                        game.turnLeft();

                                    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                                        game.turnRight();

                                    if (e.getKeyCode() == KeyEvent.VK_SPACE)
                                    {
//                                        if (pause.getText().equals("Pause"))
//                                        {
//                                            game.pause();
//                                            pause.setText("Resume");
//                                        }
//                                        else
//                                        {
//                                            game.resume();
//                                            GamePage.this.requestFocus();
//                                            pause.setText("Pause");
//                                        }
                                        pausing();
                                    }
                                }

                                @Override
                                public void keyReleased(KeyEvent e) {
                                    if (e.getKeyCode() == KeyEvent.VK_LEFT)
                                        game.stopTurningLeft();

                                    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                                        game.stopTurningRight();
                                }
                            });
        pause = new JButton("Pause");
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if (pause.getText().equals("Pause"))
//                {
//                    game.pause();
//                    pause.setText("Resume");
//                }
//                else
//                {
//                    game.resume();
//                    GamePage.this.requestFocus();
//                    pause.setText("Pause");
//                }
                pausing();
            }
        });
        add(pause);
    }

    private void pausing() {
        game.pause();
        PauseWindow pauseWindow = new PauseWindow(){
            public void onResume()
            {
                super.onResume();
                game.resume();
                requestFocus();
            }
            public void onSound()
            {
                super.onSound();
                mainWindow.toggleSound();
            }
            public void onBack()
            {
                super.onBack();
                game.onGameOver(game.getState());
            }
        };
        pauseWindow.setVisible(true);
    }

    @Override
    public void paint(Graphics graphics)
    {
        super.paint(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, configuration.getInteger("/config/settings/window/size/width"),
                    configuration.getInteger("/config/settings/window/size/height"));
            game.update().draw(graphics2D);
        } catch (XPathExpressionException e) {
            (new ErrorWindow(e, "Error reading xml")).setVisible(true);
        }
        graphics2D.drawString("Score: " + game.getScore(), 10, 20);
        paintComponents(graphics);
    }
}
