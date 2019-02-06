package plugin.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JComponent {
    private final static int WHEEL_SIZE = 600;

    public JFrame createWheel() {
        JFrame wheel = new JFrame();
        wheel.setUndecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        wheel.setBackground(new Color(0,0,0,0));
        wheel.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        Point point = MouseInfo.getPointerInfo().getLocation();
        wheel.setLayout(null);
        wheel.setBounds((int)(point.getX() - WHEEL_SIZE/2),
                (int)(point.getY() - WHEEL_SIZE/2), WHEEL_SIZE, WHEEL_SIZE);
        return wheel;
    }

    public void setBackground(JFrame wheel) {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(getClass().getResource("/images/wheel_background.png"));
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