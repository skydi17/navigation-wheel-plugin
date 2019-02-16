package plugin.listener;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WheelWindowsListener implements WindowListener {

    private final JFrame wheel;

    public  WheelWindowsListener(JFrame wheel) {
        this.wheel = wheel;
    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {
        wheel.dispose();
    }

}
