package plugin.ui;

import plugin.listener.WheelFocusListener;

import javax.swing.*;
import java.awt.*;

public class NavigationWheel extends JComponent {
    private final static int WHEEL_SIZE = 600;

    public JFrame createWheel() {
        JFrame wheel = new JFrame();
        wheel.setUndecorated(true);
        wheel.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        Point point = MouseInfo.getPointerInfo().getLocation();
        point.setLocation(point.getX() - WHEEL_SIZE/2,
                point.getY() - WHEEL_SIZE/2);
        wheel.setLocation(point);
        Dimension size = new Dimension();
        size.setSize(WHEEL_SIZE, WHEEL_SIZE);
        wheel.setMinimumSize(size);
        wheel.setMaximumSize(size);
        wheel.setSize(WHEEL_SIZE, WHEEL_SIZE);
        wheel.setOpacity(0.9f);
        wheel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        wheel.addFocusListener(new WheelFocusListener(wheel));
        wheel.setLayout(null);
        wheel.setVisible(true);
        return wheel;
    }
}