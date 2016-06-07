package controllers;

import models.ObstacleType;
import models.State;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by HP PC on 5/19/2016.
 */
public class Game {
    State state;
    JPanel gamePage;
    Configuration configuration;
    Timer timer;
    private boolean isPaused = false;

    public Game(State state, Configuration configuration) throws XPathExpressionException {
        this.state = state;
        this.configuration = configuration;
        state.setTime(System.currentTimeMillis());
        timer = new Timer(configuration.getInteger("config/settings/quality/framedelay"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePage.repaint();
            }
        });
    }

    public void onGameOver(State state)
    {
        timer.stop();
    }

    public void start() {
        timer.start();
    }

    public void pause()
    {
        isPaused = true;
        timer.stop();
    }

    public State update() throws XPathExpressionException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (isPaused)
            return state;
        state.addSpeed(configuration.getDouble("config/game/gettinghardrate"));
        long dt = System.currentTimeMillis() - state.getTime();
        if (state.getObstacle().isAlive())
        {
            state.getObstacle().update(dt);
            if (approachTest())
            {
                if (state.getObstacle().getType() == ObstacleType.BLINKING_SIDE_OBSTACLE)
                {
                    state.getObstacle().changeSide();
                }
                new SoundClip(getClass().getResource("/resources/handle.wav")).play();

            }
            if (hitTest())
            {
                new SoundClip(getClass().getResource("/resources/collision.wav")).play();
                onGameOver(state);
            }
        }
        else
        {
            state.generateObstacle();
        }
        // rotate the piece
        state.getPiece().rotate(dt * state.getAngularVelocity() * configuration.getDouble("config/game/piece/speed") * state.getSpeedCoefficient());
        // // TODO: 5/20/2016 update state and keep the time
        state.addTime(dt);
        return state;
    }

    public void turnLeft() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (state.getAngularVelocity() != -1)
            new SoundClip(getClass().getResource("/resources/steer.wav")).play();

        state.setAngularVelocity(-1);
    }

    public void turnRight() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (state.getAngularVelocity() != 1)
            new SoundClip(getClass().getResource("/resources/steer.wav")).play();

        state.setAngularVelocity(1);
    }

    public void stopTurningLeft() {
        if (state.getAngularVelocity() == -1)
            state.setAngularVelocity(0);
    }

    public void stopTurningRight() {
        if (state.getAngularVelocity() == 1)
            state.setAngularVelocity(0);
    }

    private boolean hitTest()
    {
        return state.getPiece().hitTest(state.getObstacle());
    }

    private boolean approachTest()
    {
        return state.getPiece().approachTest(state.getObstacle());
    }

    public long getScore() {
        return state.getScore();
    }

    public void setPage(JPanel page) {
        this.gamePage = page;
    }

    public Timer getTimer() {
        return timer;
    }

    public void resume() {
        state.setTime(System.currentTimeMillis());
        isPaused = false;
        timer.start();
    }

    public State getState() {
        return state;
    }
}
