package plugin.ui;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

public class FileButton extends JButton {

    private final int FILE_BUTTON_WEIGHT = 125;
    private final int FILE_BUTTON_HEIGHT = 25;
    private VirtualFile virtualFile;
    private CloseButton closeButton;
    private boolean hasErrors;

    public FileButton(VirtualFile virtualFile) {
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

    public FileButton createFileButton(double step,
                                       int filesLength,
                                       int R,
                                       int X,
                                       int Y) {
        FileButton file = this;
        int x = (int) (X + R/2 + R/2*Math.cos(step - Math.PI/filesLength));
        int y = (int) (Y + R/2 + R/2*Math.sin(step - Math.PI/filesLength));
        file.setText(virtualFile.getName());
        file.setBounds(x, y, FILE_BUTTON_WEIGHT, FILE_BUTTON_HEIGHT);
        file.setEnabled(true);
        file.setVisible(true);
        file.setFocusable(Boolean.FALSE);
        return file;
    }
}
