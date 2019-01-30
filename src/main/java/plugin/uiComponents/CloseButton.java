package plugin.uiComponents;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

public class CloseButton extends JButton {
    private VirtualFile virtualFile;

    public CloseButton(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public void setVirtualFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }
}
