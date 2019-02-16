package plugin.ui;

import com.intellij.util.ui.UIUtil;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JComponent {
    private final int WHEEL_SIZE;
    final double SCALE = 0.5;

    public NavigationWheel() {
        WHEEL_SIZE = 600;
    }

    public JFrame createWheel(Boolean closeFileOperation) {
        JFrame wheel = new JFrame();
        wheel.setUndecorated(true);
        wheel.setBackground(new Color(0,0,0,0));
        wheel.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        wheel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth()/2;
        int centerY = (int) screenSize.getHeight()/2;
        if (!closeFileOperation) {
            try {
                Robot robot = new Robot();
                robot.mouseMove(centerX, centerY);
            } catch (Exception e) {

            }
        }
        wheel.setLayout(null);
        wheel.setBounds(centerX - WHEEL_SIZE/2,
                    centerY - WHEEL_SIZE/2, WHEEL_SIZE, WHEEL_SIZE);
        return wheel;
    }

    public void setBackground(JFrame wheel) {
        BufferedImage image = null;
        try {
            if (UIUtil.isRetina()) {
                if (UIUtil.isUnderDarcula()) {
                    image = ImageIO.read(getClass().getResource("/images/wheel@2x_dark.png"));
                } else {
                    image = ImageIO.read(getClass().getResource("/images/wheel1@2x.png"));
                }
                image = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, (int) (image.getWidth() * SCALE), (int) (image.getHeight() * SCALE));
            } else {
                if (UIUtil.isUnderDarcula()) {
                    image = ImageIO.read(getClass().getResource("/images/wheel_dark.png"));
                } else {
                    image = ImageIO.read(getClass().getResource("/images/wheel1.png"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        wheel.setContentPane(new TranslucentPane());
        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(background.getX(), background.getY(),
                WHEEL_SIZE, WHEEL_SIZE);
        wheel.add(background);
    }
}