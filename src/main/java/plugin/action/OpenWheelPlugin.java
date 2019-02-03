package plugin.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.listener.UserMouseListener;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;
import plugin.ui.NavigationWheel;

import javax.swing.*;
import java.util.ArrayList;

public class OpenWheelPlugin extends AnAction {
    private final static int INNER_R = 50;
    private final static int X = 67, Y = 60, R = 400;


    public OpenWheelPlugin() {
        super("Open");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        FileEditorManager manager = FileEditorManager.getInstance(project);
        if (manager.getOpenFiles().length != 0) {
            NavigationWheel wheel = new NavigationWheel();
            viewBar(project, wheel.createWheel());
        } else {
            Messages.showMessageDialog(project, "There aren't any opened files for showing on Navigation Wheel.", "Information", Messages.getInformationIcon());
        }
    }

    public static void viewBar(Project project, JFrame wheel) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile files[] = manager.getOpenFiles();

        ArrayList<FileButton> fileButtons = new ArrayList<>(files.length);
        UserMouseListener userMouseListener = new UserMouseListener(X, Y, INNER_R, R, project, wheel);

        double step = 0;
        for (int i = 0; i < files.length; i++) {
            FileButton file = new FileButton(files[i]);
            file.createFileButton(step, files.length, manager, wheel);
            file.addMouseListener(userMouseListener);
            file.addMouseMotionListener(userMouseListener);
            wheel.add(file);
            fileButtons.add(file);

            CloseButton closeButton = new CloseButton(file);
            closeButton.createCloseButton(step, files.length, manager, wheel, project);
            file.setCloseButton(closeButton);
            wheel.add(closeButton);
            step = step + 2 * Math.PI/files.length;
        }
        userMouseListener.setFileButtons(fileButtons);
        wheel.addMouseListener(userMouseListener);
        wheel.addMouseMotionListener(userMouseListener);
    }
}