package plugin.ui;

import com.intellij.openapi.diagnostic.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JDialog {
    private final int wheelHeight;
    private final int wheelWidth;
    private final Logger logger;

    public NavigationWheel(int height, int width) {
        this.wheelHeight = height;
        this.wheelWidth = width;
        this.logger = Logger.getInstance(NavigationWheel.class);
        initializeDialog();
    }

    /**
     * Initializes the dialog properties.
     */
    private void initializeDialog() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setModal(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        centerMouseCursor();
        setLayout(null);
        setBounds(0, 0, wheelWidth, wheelHeight);
    }

    /**
     * Centers the mouse cursor on the screen.
     */
    private void centerMouseCursor() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;
        try {
            Robot robot = new Robot();
            robot.mouseMove(centerX, centerY);
        } catch (AWTException e) {
            logger.warn("Mouse cursor wasn't moved to the center due to an error", e);
        }
    }

    /**
     * Sets the background image of the navigation wheel based on the current theme.
     */
    public void setBackgroundImage() {
        BufferedImage image = loadBackgroundImage();
        if (image != null) {
            JLabel background = new JLabel(new ImageIcon(image));
            background.setBounds(
                    (wheelWidth - image.getWidth()) / 2,
                    (wheelHeight - image.getHeight()) / 2,
                    image.getWidth(),
                    image.getHeight()
            );
            add(background);
        }
    }

    /**
     * Loads the appropriate background image based on the current UI theme.
     *
     * @return the loaded background image, or null if loading failed.
     */
    private BufferedImage loadBackgroundImage() {
        BufferedImage image = null;
        try {
            String theme = UIManager.getLookAndFeel().getName().toLowerCase();
            String imagePath = theme.contains("darcula") || theme.contains("dark")
                    ? "/images/wheel_dark.png"
                    : "/images/wheel.png";
            image = ImageIO.read(getClass().getResource(imagePath));
        } catch (Exception e) {
            logger.error("Failed to load the wheel background image due to an error", e);
        }
        return image;
    }
}