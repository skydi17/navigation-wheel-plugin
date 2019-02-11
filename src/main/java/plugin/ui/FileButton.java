package plugin.ui;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

public class FileButton extends JButton {

    private final int FILE_BUTTON_WEIGHT;
    private final int FILE_BUTTON_HEIGHT;
    private VirtualFile virtualFile;
    private CloseButton closeButton;
    private boolean hasErrors;
    private int originalX;
    private int originalY;

    public FileButton(VirtualFile virtualFile) {
        FILE_BUTTON_WEIGHT = 125;
        FILE_BUTTON_HEIGHT = 25;
        this.virtualFile = virtualFile;
        hasErrors = false;
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

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
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
        file.setOriginalX((int) (X + R/2 + R/2*Math.cos(step - Math.PI/filesLength)));
        file.setOriginalY((int) (Y + R/2 + R/2*Math.sin(step - Math.PI/filesLength)));
        file.setText(virtualFile.getName());
        file.setBounds(file.getOriginalX(), file.getOriginalY(), FILE_BUTTON_WEIGHT, FILE_BUTTON_HEIGHT);
        file.setEnabled(true);
        file.setVisible(true);
        file.setFocusable(Boolean.FALSE);
        return file;
    }
}
