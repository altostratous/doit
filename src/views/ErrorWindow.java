package views;

import javax.swing.*;

/**
 * Created by HP PC on 5/18/2016.
 */
public class ErrorWindow extends JFrame{
    public ErrorWindow(Exception e, String message) {
        e.printStackTrace();
        System.exit(1);
    }

    public static void viewError(Exception e) {
        viewError(e, "Exception occurred!");
    }

    public static void viewError(Exception e, String message)
    {
        ErrorWindow errorWindow = new ErrorWindow(e, message);
        errorWindow.setVisible(true);
    }
}
