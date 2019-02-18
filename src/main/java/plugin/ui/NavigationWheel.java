package plugin.ui;

import com.intellij.util.ui.UIUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JComponent {
    private final int WHEEL_HEIGHT, WHEEL_WIDTH;
    private final int PAINTED_R = 295;
    private final double SCALE = 0.5;

    public NavigationWheel(int height, int width){
        WHEEL_HEIGHT = height;
        WHEEL_WIDTH = width;
    }

    public JFrame createWheel() {
        JFrame wheel = new JFrame();
        wheel.setUndecorated(Boolean.TRUE);
        wheel.setBackground(new Color(0,0,0,0));
        wheel.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        wheel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth()/2;
        int centerY = (int) screenSize.getHeight()/2;
        try {
            Robot robot = new Robot();
            robot.mouseMove(centerX, centerY);
        } catch (Exception e) {

        }
        wheel.setLayout(null);
        wheel.setBounds(0, 0, WHEEL_WIDTH, WHEEL_HEIGHT);
        return wheel;
    }

    public void setBackground(JFrame wheel) {
        BufferedImage image = null;
        ImageIcon imageIcon = null;
        try {
            if (UIUtil.isRetina()) {
                if (UIUtil.isUnderDarcula()) {
                    image = ImageIO.read(getClass().getResource("/images/wheel_illustrator_retina_4.png"));
                } else {
                    image = ImageIO.read(getClass().getResource("/images/wheel_illustrator_retina_6.png"));
                }
                Image scaledImage = image.getScaledInstance(
                        (int)(SCALE * image.getWidth()),
                        (int)(SCALE * image.getHeight()),
                        Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(scaledImage);
            } else {
                if (UIUtil.isUnderDarcula()) {
                    image = ImageIO.read(getClass().getResource("/images/wheel_dark.png"));
                } else {
                    image = ImageIO.read(getClass().getResource("/images/wheel_illustrator_5.png"));
                }
                imageIcon = new ImageIcon(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        wheel.setContentPane(new TranslucentPane());
        JLabel background = new JLabel(imageIcon);
        wheel.setLayout(null);
        background.setBounds(WHEEL_WIDTH/2 - PAINTED_R, WHEEL_HEIGHT/2 - PAINTED_R,
                image.getWidth(), image.getHeight());
        wheel.add(background);
    }
}