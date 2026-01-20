package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.service.WheelService;
import plugin.ui.NavigationWheel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseButtonListener implements ActionListener {
    private final FileEditorManager fileEditorManager;
    private final Project project;
    private final NavigationWheel wheel;
    private final VirtualFile virtualFile;

    public CloseButtonListener(Project project, NavigationWheel wheel, VirtualFile virtualFile) {
        this.project = project;
        this.fileEditorManager = FileEditorManager.getInstance(project);
        this.wheel = wheel;
        this.virtualFile = virtualFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        closeFileAndRefresh();
    }

    private void closeFileAndRefresh() {
        fileEditorManager.closeFile(virtualFile);
        SwingUtilities.invokeLater(() -> WheelService.refreshWheel(project, wheel));
    }

}