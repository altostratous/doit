package views;

import controllers.Configuration;

import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by HP PC on 5/18/2016.
 */
public class FirstPage extends JPanel {
    private MainWindow mainWindow;

    public FirstPage(Configuration configuration, MainWindow mainWindow) throws XPathExpressionException {
        this.mainWindow = mainWindow;
        // TODO: 5/19/2016  use our own button
        JButton startButton = new JButton("Start");
        JButton soundButton = new JButton("Sound Off");
        add(startButton);
        add(soundButton);
        // TODO: 5/19/2016 Set layout
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    onStart();
                } catch (XPathExpressionException e1) {
                    ErrorWindow.viewError(e1);
                }
            }
        });

        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (soundButton.getText().equals("Sound On")) {
                    onSoundOn();
                    soundButton.setText("Sound Off");
                } else
                {
                    onSoundOff();
                    soundButton.setText("Sound On");
                }
            }
        });
    }

    public void onStart() throws XPathExpressionException {
    }

    public void onSoundOn()
    {

    }

    public void onSoundOff()
    {

    }
}
