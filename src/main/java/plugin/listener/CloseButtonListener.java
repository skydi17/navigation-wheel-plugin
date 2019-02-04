package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.action.OpenWheelPlugin;

import javax.swing.*;
import java.applet.Applet;
import java.awt.event.*;

public class CloseButtonListener extends Applet implements ActionListener {

    FileEditorManager fileEditorManager;
    Project project;
    JFrame wheel;
    VirtualFile virtualFile;

    public CloseButtonListener (Project project, JFrame wheel, VirtualFile virtualFile) {
        this.project = project;
        this.fileEditorManager = FileEditorManager.getInstance(project);
        this.wheel = wheel;
        this.virtualFile = virtualFile;

    }

    public void actionPerformed(ActionEvent e) {
        fileEditorManager.closeFile(virtualFile);
        if (fileEditorManager.getOpenFiles().length == 0) {
            wheel.dispose();
            return;
        }
        wheel.getContentPane().removeAll();
        wheel.getContentPane().repaint();
        OpenWheelPlugin.viewBar(project, wheel);
    }
}
