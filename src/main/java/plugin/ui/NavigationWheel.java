package plugin.ui;

import com.intellij.util.ui.UIUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JComponent {
    private final int WHEEL_SIZE;

    public NavigationWheel() {
        if (UIUtil.isRetina()) {
            WHEEL_SIZE = 1200;
        } else {
            WHEEL_SIZE = 600;
        }
    }

    public JFrame createWheel(int x, int y) {
        JFrame wheel = new JFrame();
        wheel.setUndecorated(true);
        wheel.setBackground(new Color(0,0,0,0));
        wheel.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        wheel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Point point = MouseInfo.getPointerInfo().getLocation();
        wheel.setLayout(null);
        if (x == -1 && y == -1) {
            wheel.setBounds((int) (point.getX() - WHEEL_SIZE / 2),
                    (int) (point.getY() - WHEEL_SIZE / 2), WHEEL_SIZE, WHEEL_SIZE);
        } else {
            wheel.setBounds(x, y, WHEEL_SIZE, WHEEL_SIZE);
        }
        return wheel;
    }

    public void setBackground(JFrame wheel) {
        BufferedImage image = null;
        try
        {
            if (UIUtil.isRetina()) {
                if (UIUtil.isUnderDarcula()) {
                    image = ImageIO.read(getClass().getResource("/images/wheel2x_dark.png"));
                } else {
                    image = ImageIO.read(getClass().getResource("/images/wheel2x.png"));
                }
            } else {
                if (UIUtil.isUnderDarcula()) {
                    image = ImageIO.read(getClass().getResource("/images/wheel_dark.png"));
                } else {
                    image = ImageIO.read(getClass().getResource("/images/wheel.png"));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        wheel.setContentPane(new TranslucentPane());
        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(background.getX(), background.getY(),
                600, 600);
        wheel.add(background);
    }
}