package plugin.listener;

import plugin.ui.NavigationWheel;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LostFocusWindowListener extends WindowAdapter {

    private final NavigationWheel wheel;

    public LostFocusWindowListener(NavigationWheel wheel) {
        this.wheel = wheel;
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        if (e.getOppositeWindow() != null && SwingUtilities.isDescendingFrom(e.getOppositeWindow(), e.getWindow())) {
            return;
        }
        wheel.dispose();
    }
}
