package views;

import controllers.Configuration;
import controllers.Game;
import controllers.SoundClip;
import models.State;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

/**
 * Created by HP PC on 5/18/2016.
 */
public class MainWindow extends JFrame{
    JPanel firstPage;
    JPanel gamePage;
    ScorePage scorePage;
    Configuration configuration;
    Game game;
    SoundClip soundClip;
    boolean sound = true;
    public MainWindow(Configuration configuration) throws XPathExpressionException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.configuration = configuration;
        soundClip = new SoundClip(MainWindow.class.getResource("/resources/music.wav"));
        soundClip.play();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);


        firstPage = new FirstPage(configuration, this){
            // TODO: 5/19/2016 Override actions here
            @Override
            public void onStart() throws XPathExpressionException {
                super.onStart();
                MainWindow.this.onStart();
            }

            @Override
            public void onSoundOn(){
                super.onSoundOn();
                toggleSound();
            }

            @Override
            public void onSoundOff() {
                super.onSoundOff();
                toggleSound();
            }
        };

        showPage(firstPage);

    }

    public void toggleSound()
    {
        if (!sound) {
            try {
                soundClip = new SoundClip(getClass().getResource("/resources/music.wav"));
            } catch (Exception e) {
                ErrorWindow.viewError(e, "Audio file error");
            }
            soundClip.play();
            sound = true;
        }
        else {
            soundClip.stop();
            sound = false;
        }
    }
    private void showPage(JPanel page)
    {
        if(page != firstPage)
            if (firstPage != null)
                firstPage.setVisible(false);
        if (page != gamePage )
            if (gamePage != null)
                gamePage.setVisible(false);
        if (page != scorePage )
            if (scorePage != null)
                scorePage.setVisible(false);
        add(page);
        page.setVisible(true);
        page.requestFocus();
    }
    private void onStart() throws XPathExpressionException {
        State state = new State(configuration);
        game = new Game(state, configuration){
            @Override
            public void onGameOver(State state) {
                super.onGameOver(state);
                try {
                    MainWindow.this.onGameOver();
                }
                catch (Exception ex)
                {
                    ErrorWindow.viewError(ex);
                }
            }
        };
        gamePage = new GamePage(configuration, game, this)
        {
            // override actions here
        };
        game.setPage(gamePage);
        showPage(gamePage);
        game.start();


    }

    private void onGameOver() throws XPathExpressionException {
        if (scorePage == null) {
            scorePage = new ScorePage(configuration, game, this) {
                // override events here
                @Override
                public void onStart() throws XPathExpressionException {
                    super.onStart();
                    MainWindow.this.onStart();
                }

                @Override
                public void onSoundOn() {
                    super.onSoundOn();
                    try {
                        soundClip = new SoundClip(getClass().getResource("/resources/music.wav"));
                    } catch (Exception e) {
                        ErrorWindow.viewError(e, "Audio file error");
                    }
                    soundClip.play();
                }

                @Override
                public void onSoundOff() {
                    super.onSoundOff();
                    soundClip.stop();
                }
            };
        }
        else
        {
            scorePage.update(game);
        }
        showPage(scorePage);
    }
}
