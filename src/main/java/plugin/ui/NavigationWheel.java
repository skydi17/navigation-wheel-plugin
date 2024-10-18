package plugin.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JDialog {
    private final Logger logger = Logger.getInstance(NavigationWheel.class);

    private final int wheelHeight;
    private final int wheelWidth;

    public NavigationWheel(int height, int width) {
        this.wheelHeight = height;
        this.wheelWidth = width;
        initializeDialog();
    }

    /**
     * Initializes the dialog properties.
     */
    private void initializeDialog() {
        setUndecorated(true);
        setBackground(new Color(1, 1, 1, 1));
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setModal(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setBounds(0, 0, wheelWidth, wheelHeight);
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
            String imagePath = JBColor.isBright() ? "/images/wheel_white.png"
                    : "/images/wheel_dark.png";
            image = ImageIO.read(getClass().getResource(imagePath));
        } catch (Exception e) {
            logger.error("Failed to load the wheel background image due to an error", e);
        }
        return image;
    }
}