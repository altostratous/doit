package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by HP PC on 5/22/2016.
 */
public class PauseWindow extends JDialog {
    JButton sound;
    JButton back;
    JButton resume;
    public PauseWindow()
    {
        sound = new JButton("Sound");
        sound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSound();
            }
        });
        back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBack();
            }
        });
        resume = new JButton("Resume");
        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onResume();
            }
        });
        add(resume);
        add(back);
        add(sound);
        setModal(true);
        setSize(300, 200);Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 -getSize().width/2, dim.height/2-getSize().height/2);
        setLayout(new FlowLayout());
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                onResume();
                setVisible(false);
            }
        });
    }

    public void onResume() {
        setVisible(false);
    }
    public void onSound()
    {


    }
    public void onBack(){
        setVisible(false);
    }
}
