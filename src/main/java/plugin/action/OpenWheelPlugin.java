package plugin.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.listener.UserClicksListener;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;
import plugin.ui.NavigationWheel;

import javax.swing.*;
import java.util.ArrayList;

public class OpenWheelPlugin extends AnAction {
    private final static int WHEEL_SIZE = 600, INNER_R = 50;
    private final static int X = 67, Y = 60, R = 400;


    public OpenWheelPlugin() {
        super("Open");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        NavigationWheel wheel = new NavigationWheel();
        viewBar(project, wheel.createWheel());
    }

    public static void viewBar(Project project, JFrame wheel) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile files[] = manager.getOpenFiles();

        ArrayList<FileButton> fileButtons = new ArrayList<>(files.length);

        double step = 0;
        for (int i = 0; i < files.length; i++) {
            FileButton file = new FileButton(files[i]);
            file.createFileButton(step, files.length, manager, wheel);
            wheel.add(file);
            fileButtons.add(file);

            CloseButton closeButton = new CloseButton(file);
            closeButton.createCloseButton(step, files.length, manager, wheel, project);
            file.setCloseButton(closeButton);
            wheel.add(closeButton);
            step = step + 2 * Math.PI/files.length;
        }
        wheel.setSize(WHEEL_SIZE, WHEEL_SIZE);
        wheel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wheel.setLayout(null);
        wheel.setVisible(true);

        UserClicksListener userClicksListener = new UserClicksListener(X, Y, INNER_R, R, fileButtons, manager);
        wheel.addMouseListener(userClicksListener);
        wheel.addMouseMotionListener(userClicksListener);
    }
}