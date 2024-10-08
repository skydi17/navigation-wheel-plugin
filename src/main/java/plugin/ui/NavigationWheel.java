package plugin.ui;

import com.intellij.openapi.diagnostic.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JDialog {
    private final int WHEEL_HEIGHT, WHEEL_WIDTH;
    private final int PAINTED_R;
    private final Logger LOG;

    public NavigationWheel(int height, int width) {
        PAINTED_R = 295;
        this.WHEEL_HEIGHT = height;
        this.WHEEL_WIDTH = width;
        this.LOG = Logger.getInstance(NavigationWheel.class);
        init();
    }

    private void init() {
        this.setUndecorated(Boolean.TRUE);
        this.setBackground(new Color(0, 0, 0, 0));
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setModal(Boolean.TRUE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;
        try {
            Robot robot = new Robot();
            robot.mouseMove(centerX, centerY);
        } catch (Exception e) {
            LOG.warn("Mouse cursor wasn't moved to the centre because of error", e);
        }
        this.setLayout(null);
        this.setBounds(0, 0, WHEEL_WIDTH, WHEEL_HEIGHT);
    }

    public void setBackground(NavigationWheel wheel) {
        BufferedImage image = null;
        try {
            String theme = UIManager.getLookAndFeel().getName().toLowerCase();
            if (theme.contains("darcula") || theme.contains("dark")) {
                image = ImageIO.read(getClass().getResource("/images/wheel_dark.png"));
            } else {
                image = ImageIO.read(getClass().getResource("/images/wheel.png"));
            }
        } catch (Exception e) {
            LOG.error("Wheel background wasn't loaded because of the error", e);
        }
        JLabel background = new JLabel(new ImageIcon(image));
        wheel.setLayout(null);
        background.setBounds(WHEEL_WIDTH / 2 - PAINTED_R, WHEEL_HEIGHT / 2 - PAINTED_R,
                image.getWidth(), image.getHeight());
        wheel.add(background);
    }
}