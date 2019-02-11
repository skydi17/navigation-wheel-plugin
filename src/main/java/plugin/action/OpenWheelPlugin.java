package plugin.action;

import com.intellij.codeInsight.CodeSmellInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CodeSmellDetector;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.UIUtil;
import plugin.listener.UserMouseListener;
import plugin.listener.WheelFocusListener;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;
import plugin.ui.NavigationWheel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenWheelPlugin extends AnAction {
    //private static int INNER_R = 40;
    private static int X = 10, Y = 70, R = 430;
    private static NavigationWheel navigationWheel;
    private static boolean needCodeAnalysis = false;


    public OpenWheelPlugin() {
        super("Open");
    }

    public OpenWheelPlugin(Boolean needCodeAnalysis) {
        this.needCodeAnalysis = needCodeAnalysis;
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project != null) {
            FileEditorManager manager = FileEditorManager.getInstance(project);
            if (manager.getOpenFiles().length > 1) {
                createWheel(project, null, -1, -1);
            } else {
                Messages.showMessageDialog(project, "Not enough files opened.", "Information", Messages.getInformationIcon());
            }
        }
    }

    public static void createWheel(Project project, VirtualFile closedFile, int x, int y) {
        navigationWheel = new NavigationWheel();
        viewBar(project, navigationWheel.createWheel(x, y), closedFile);
    }

    public static void viewBar(Project project, JFrame wheel, VirtualFile closedFile) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile files[] = manager.getOpenFiles();

        ArrayList<FileButton> fileButtons = new ArrayList<>(files.length);
        UserMouseListener userMouseListener = new UserMouseListener(X, Y, R, project, wheel);

        double step = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i] != closedFile) {
                FileButton file = new FileButton(files[i]);
                file.createFileButton(step, files.length, R, X, Y);
                file.addMouseListener(userMouseListener);
                file.addMouseMotionListener(userMouseListener);
                wheel.getLayeredPane().add(file);
                fileButtons.add(file);

                CloseButton closeButton = new CloseButton(file);
                closeButton.createCloseButton(wheel, project);
                file.setCloseButton(closeButton);
                wheel.getLayeredPane().add(closeButton);
            } else {
                manager.closeFile(closedFile);
            }
            step = step + 2 * Math.PI/files.length;
        }
        if (needCodeAnalysis) {
            runCodeAnalysis(manager, fileButtons);
            needCodeAnalysis = false;
        }
        userMouseListener.setFileButtons(fileButtons);
        wheel.addMouseListener(userMouseListener);
        wheel.addMouseMotionListener(userMouseListener);
        wheel.addFocusListener(new WheelFocusListener(wheel));
        navigationWheel.setBackground(wheel);
        wheel.setVisible(true);
    }

    private static void runCodeAnalysis(FileEditorManager manager, ArrayList<FileButton> fileButtons) {
        for (FileButton fileButton : fileButtons) {
            List<CodeSmellInfo> codeSmellInfos = CodeSmellDetector.getInstance(manager.getProject()).findCodeSmells(Arrays.asList(fileButton.getVirtualFile()));
            for (CodeSmellInfo codeSmellInfo : codeSmellInfos) {
                if (codeSmellInfo.getSeverity() == HighlightSeverity.ERROR) {
                    fileButton.setHasErrors(true);
                    if (UIUtil.isUnderDarcula()) {
                        fileButton.setBackground(Color.LIGHT_GRAY);
                        fileButton.getCloseButton().setBackground(Color.LIGHT_GRAY);
                    } else {
                        fileButton.setBackground(Color.PINK);
                        fileButton.getCloseButton().setBackground(Color.PINK);
                    }
                    fileButton.repaint();
                }
            }
        }
    }
}