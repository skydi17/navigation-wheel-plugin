package plugin.ui;

import com.intellij.util.ui.UIUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JDialog {
    private final int WHEEL_HEIGHT, WHEEL_WIDTH;
    private final int PAINTED_R;

    public NavigationWheel(int height, int width){
        PAINTED_R = 295;
        this.WHEEL_HEIGHT = height;
        this.WHEEL_WIDTH = width;
    }

    public NavigationWheel init() {
        NavigationWheel wheel = this;
        wheel.setUndecorated(Boolean.TRUE);
        wheel.setBackground(new Color(0,0,0,0));
        wheel.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        wheel.setModal(Boolean.TRUE);
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

    public void setBackground(NavigationWheel wheel) {
        BufferedImage image = null;
        try {
            if (UIUtil.isUnderDarcula()) {
                image = ImageIO.read(getClass().getResource("/images/wheel_dark.png"));
            } else {
                image = ImageIO.read(getClass().getResource("/images/wheel.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        JLabel background = new JLabel(new ImageIcon(image));
        wheel.setLayout(null);
        background.setBounds(WHEEL_WIDTH/2 - PAINTED_R, WHEEL_HEIGHT/2 - PAINTED_R,
                image.getWidth(), image.getHeight());
        wheel.add(background);
    }
}