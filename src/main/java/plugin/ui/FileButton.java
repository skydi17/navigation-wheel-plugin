package plugin.ui;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileButton extends JButton {

    private final int FILE_BUTTON_WEIGHT = 125;
    private final int FILE_BUTTON_HEIGHT = 25;
    private final int X = 67, Y = 60, R = 400;
    PinButtonStatus status;
    private double locX;
    private double locY;
    private VirtualFile virtualFile;
    private CloseButton closeButton;

    public FileButton(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
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

    public FileButton createFileButton( double step,
                                        int filesLength,
                                        final FileEditorManager manager,
                                        final JFrame wheel) {
        locX = X + R/2 + R/2*Math.cos(step - Math.PI/filesLength);
        locY = Y + R/2 + R/2*Math.sin(step - Math.PI/filesLength);
        FileButton file = this;
        file.setText(virtualFile.getName());
        file.setBounds((int) (locX - 50), (int) (locY + 10), FILE_BUTTON_WEIGHT, FILE_BUTTON_HEIGHT);
        file.setEnabled(true);
        file.setVisible(true);
        file.setFocusable(Boolean.FALSE);

        file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.openFile(file.getVirtualFile(), Boolean.TRUE);
                wheel.dispose();
            }
        });
        return file;
    }
}
