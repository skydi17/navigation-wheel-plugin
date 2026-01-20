package plugin.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.JBColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NavigationWheel extends JPanel {
    private final Logger logger = Logger.getInstance(NavigationWheel.class);

    private int wheelHeight;
    private int wheelWidth;
    private JBPopup popup;

    public NavigationWheel(int height, int width) {
        super(null);
        this.wheelHeight = height;
        this.wheelWidth = width;
        initializePanel();
    }

    public void updateDimensions(int height, int width) {
        this.wheelHeight = height;
        this.wheelWidth = width;
        setPreferredSize(new Dimension(wheelWidth, wheelHeight));
        setSize(wheelWidth, wheelHeight);
    }

    private void initializePanel() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setBorder(null);
        setFocusable(true);
        setPreferredSize(new Dimension(wheelWidth, wheelHeight));
        setSize(wheelWidth, wheelHeight);

        setLayout(null);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    public void setPopup(JBPopup popup) {
        this.popup = popup;
    }

    public void dispose() {
        if (popup != null) {
            popup.cancel();
        }
    }

    public void clearButtons() {
        for (Component component : getComponents()) {
            if (component instanceof FileButton || component instanceof CloseButton || component instanceof JLabel) {
                remove(component);
            }
        }
        revalidate();
        repaint();
    }

    public void setBackgroundImage() {
        BufferedImage image = loadBackgroundImage();
        if (image != null) {
            JLabel background = new JLabel(new ImageIcon(image));
            background.setOpaque(false);
            background.setBounds(
                    (wheelWidth - image.getWidth()) / 2,
                    (wheelHeight - image.getHeight()) / 2,
                    image.getWidth(),
                    image.getHeight()
            );
            add(background);
            revalidate();
            repaint();
        }
    }

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