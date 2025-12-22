package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenFileAndCloseWheelListener implements ActionListener {

    private final FileEditorManager fileEditorManager;
    private final VirtualFile virtualFile;
    private final Window wheel;

    public OpenFileAndCloseWheelListener(FileEditorManager fileEditorManager,
                                         VirtualFile virtualFile,
                                         Window wheel) {
        this.fileEditorManager = fileEditorManager;
        this.virtualFile = virtualFile;
        this.wheel = wheel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fileEditorManager.openFile(virtualFile, true);
        if (wheel != null) {
            wheel.dispose();
        }
    }
}
