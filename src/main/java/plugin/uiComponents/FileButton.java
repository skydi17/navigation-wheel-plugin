package plugin.uiComponents;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

public class FileButton extends JButton {
    PinButtonStatus status;
    private double locX;
    private double locY;
    private VirtualFile virtualFile;
    private CloseButton closeButton;

    public FileButton(VirtualFile virtualFile, double x, double y) {
        this.virtualFile = virtualFile;
        this.locX = x;
        this.locY = y;
        this.closeButton = closeButton;
        status = PinButtonStatus.UNPINNED;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public double getLocX() {
        return locX;
    }

    public void setLocX(double locX) {
        this.locX = locX;
    }

    public double getLocY() {
        return locY;
    }

    public void setLocY(double locY) {
        this.locY = locY;
    }

    public CloseButton getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(CloseButton closeButton) {
        this.closeButton = closeButton;
    }
}
