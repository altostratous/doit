package controllers;

import models.State;

import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by HP PC on 5/19/2016.
 */
public class Game {
    State state;
    JPanel gamePage;
    Configuration configuration;
    Timer timer;

    public Game(State state, Configuration configuration) {
        this.state = state;
        this.gamePage = gamePage;
        this.configuration = configuration;
        state.addTime(System.currentTimeMillis() - state.getTime());
        timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePage.repaint();
            }
        });
    }

    public void onGameOver(State state)
    {
    }

    public void start() {
        timer.start();
    }

    public void pause()
    {
        timer.stop();
    }

    public State update() throws XPathExpressionException {
        long dt = System.currentTimeMillis() - state.getTime();
        if (state.getObstacle().isAlive())
        {
            state.getObstacle().update(dt);
            approachTest();
            hitTest();
        }
        else
        {
            state.generateObstacle();
        }
        // rotate the piece
        state.getPiece().rotate(state.getAngularVelocity() * configuration.getDouble("config/game/piece/speed") * state.getSpeedCoefficient());
        // // TODO: 5/20/2016 update state and keep the time
        state.addTime(dt);
        return state;
    }

    public void turnLeft() {
        state.setAngularVelocity(-1);
    }

    public void turnRight() {
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
}