package plugin.listener;

import plugin.ui.NavigationWheel;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WheelWindowsListener implements WindowListener {

    private final NavigationWheel wheel;

    public  WheelWindowsListener(NavigationWheel wheel) {
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
        if (wheel.isNeedToCloseWithoutFocus()) {
            wheel.dispose();
        } else {
            wheel.setNeedToCloseWithoutFocus(Boolean.TRUE);
        }
    }

}
