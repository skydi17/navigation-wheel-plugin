package plugin.uiComponents;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

public class FileButton extends JButton {
    PinButtonStatus status;
    private double fromAngle;
    private double toAngle;
    private VirtualFile virtualFile;

    public FileButton(VirtualFile virtualFile, double fromAngle, double toAngle) {
        this.virtualFile = virtualFile;
        this.fromAngle = fromAngle;
        this.toAngle = toAngle;
        status = PinButtonStatus.UNPINNED;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public double getFromAngle() {
        return fromAngle;
    }

    public void setFromAngle(double fromAngle) {
        this.fromAngle = fromAngle;
    }

    public double getToAngle() {
        return toAngle;
    }

    public void setToAngle(double toAngle) {
        this.toAngle = toAngle;
    }
}
