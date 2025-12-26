package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.ui.NavigationWheel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenFileAndCloseWheelListener implements ActionListener {

    private final FileEditorManager fileEditorManager;
    private final VirtualFile virtualFile;
    private final NavigationWheel wheel;

    public OpenFileAndCloseWheelListener(FileEditorManager fileEditorManager,
                                         VirtualFile virtualFile,
                                         NavigationWheel wheel) {
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
