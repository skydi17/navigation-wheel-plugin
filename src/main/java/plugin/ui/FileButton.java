package plugin.ui;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;

public class FileButton extends JButton {

    // Constants for button size
    private static final int BUTTON_WIDTH = 125;
    private static final int BUTTON_HEIGHT = 25;

    private final VirtualFile virtualFile;
    private CloseButton closeButton;
    private final int originalX;
    private final int originalY;

    /**
     * Constructor for FileButton.
     *
     * @param virtualFile The virtual file associated with this button.
     * @param step        Angle for calculating the button's position.
     * @param totalFiles  Total number of files for angle calculation.
     * @param radius      Radius for position calculation.
     * @param offsetX     Offset along the X-axis.
     * @param offsetY     Offset along the Y-axis.
     */
    public FileButton(VirtualFile virtualFile, double step, int totalFiles, int radius, int offsetX, int offsetY) {
        super(virtualFile.getName());
        this.virtualFile = virtualFile;
        this.originalX = calculatePositionX(step, totalFiles, radius, offsetX);
        this.originalY = calculatePositionY(step, totalFiles, radius, offsetY);
        initializeButton();
    }

    /**
     * Initializes button properties.
     */
    private void initializeButton() {
        setIcon(virtualFile.getFileType().getIcon());
        setBounds(originalX, originalY, BUTTON_WIDTH, BUTTON_HEIGHT);
        setEnabled(true);
        setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Calculates the position along the X-axis.
     */
    private int calculatePositionX(double step, int totalFiles, int radius, int offsetX) {
        return (int) (offsetX + radius + radius * Math.cos(step - Math.PI / totalFiles));
    }

    /**
     * Calculates the position along the Y-axis.
     */
    private int calculatePositionY(double step, int totalFiles, int radius, int offsetY) {
        return (int) (offsetY + radius + radius * Math.sin(step - Math.PI / totalFiles));
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public CloseButton getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(CloseButton closeButton) {
        this.closeButton = closeButton;
    }

    public int getOriginalX() {
        return originalX;
    }

    public int getOriginalY() {
        return originalY;
    }
}
