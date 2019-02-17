package plugin.ui;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;

public class FileButton extends JButton {

    private final int FILE_BUTTON_WEIGHT = 125;
    private final int FILE_BUTTON_HEIGHT = 25;
    private VirtualFile virtualFile;
    private CloseButton closeButton;
    private int originalX;
    private int originalY;

    public FileButton(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
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

    public void setOriginalX(int originalX) {
        this.originalX = originalX;
    }

    public int getOriginalY() {
        return originalY;
    }

    public void setOriginalY(int originalY) {
        this.originalY = originalY;
    }

    public FileButton createFileButton(double step,
                                       int filesLength,
                                       int R,
                                       int X,
                                       int Y) {
        FileButton file = this;
        file.setOriginalX((int) (X + R + R*Math.cos(step - Math.PI/filesLength)));
        file.setOriginalY((int) (Y + R + R*Math.sin(step - Math.PI/filesLength)));
        file.setIcon(virtualFile.getFileType().getIcon());
        file.setText(virtualFile.getName());
        file.setBounds(file.getOriginalX(), file.getOriginalY(), FILE_BUTTON_WEIGHT, FILE_BUTTON_HEIGHT);
        file.setEnabled(Boolean.TRUE);
        file.setVisible(Boolean.TRUE);
        file.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return file;
    }
}
