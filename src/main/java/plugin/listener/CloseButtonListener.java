package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.action.OpenWheelPlugin;
import plugin.ui.NavigationWheel;

import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseButtonListener extends Applet implements ActionListener {

    private final FileEditorManager fileEditorManager;
    private final Project project;
    private final NavigationWheel wheel;
    private final VirtualFile virtualFile;

    public CloseButtonListener (Project project, NavigationWheel wheel, VirtualFile virtualFile) {
        this.project = project;
        this.fileEditorManager = FileEditorManager.getInstance(project);
        this.wheel = wheel;
        this.virtualFile = virtualFile;

    }

    public void actionPerformed(ActionEvent e) {
        fileEditorManager.closeFile(virtualFile);
        wheel.dispose();
        if (fileEditorManager.getOpenFiles().length > 1) {
            OpenWheelPlugin.createWheel(project);
        }
    }
}
