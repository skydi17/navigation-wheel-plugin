package plugin.listener;

import plugin.ui.NavigationWheel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LostFocusWindowListener extends WindowAdapter {

    private final NavigationWheel wheel;

    public LostFocusWindowListener(NavigationWheel wheel) {
        this.wheel = wheel;
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        wheel.dispose();
    }
}
