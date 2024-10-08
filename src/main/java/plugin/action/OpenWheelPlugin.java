package plugin.action;

import com.intellij.codeInsight.CodeSmellInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CodeSmellDetector;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.listener.UserMouseListener;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;
import plugin.ui.NavigationWheel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenWheelPlugin extends AnAction {
    private static final int X = 5, Y = 70, D = 430, PAINTED_R = 295, INNER_R = 60;
    private static NavigationWheel navigationWheel;
    private static boolean needCodeAnalysis = Boolean.FALSE;
    private static int WHEEL_HEIGHT, WHEEL_WIDTH;
    private final String NOT_ENOUGH_FILES_MESSAGE = "Not enough files opened.";
    private final String TITLE_MESSAGE = "Information";


    public OpenWheelPlugin() {
        super("Open");
    }

    public OpenWheelPlugin(Boolean needCodeAnalysis) {
        this.needCodeAnalysis = needCodeAnalysis;
    }

    public void actionPerformed(AnActionEvent event) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WHEEL_HEIGHT = (int) screenSize.getHeight();
        WHEEL_WIDTH = (int) screenSize.getWidth();
        Project project = event.getProject();
        if (project != null) {
            FileEditorManager manager = FileEditorManager.getInstance(project);
            if (manager.getOpenFiles().length > 1) {
                createWheel(project);
            } else {
                Messages.showMessageDialog(project, NOT_ENOUGH_FILES_MESSAGE, TITLE_MESSAGE, Messages.getInformationIcon());
            }
        }
    }

    public static void createWheel(Project project) {
        navigationWheel = new NavigationWheel(WHEEL_HEIGHT, WHEEL_WIDTH);
        viewBar(project);
    }

    public static void viewBar(Project project) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile files[] = manager.getOpenFiles();

        ArrayList<FileButton> fileButtons = new ArrayList<>(files.length);
        UserMouseListener userMouseListener = new UserMouseListener(D, PAINTED_R, project, navigationWheel, INNER_R);

        double step = 0;
        for (int i = 0; i < files.length; i++) {
            FileButton file = new FileButton(files[i]);
            file.init(step, files.length, D / 2,
                    WHEEL_WIDTH / 2 - PAINTED_R + X, WHEEL_HEIGHT / 2 - PAINTED_R + Y);
            file.addMouseListener(userMouseListener);
            file.addMouseMotionListener(userMouseListener);
            navigationWheel.getLayeredPane().add(file);
            fileButtons.add(file);

            CloseButton closeButton = new CloseButton(file);
            closeButton.init(navigationWheel, project);
            file.setCloseButton(closeButton);
            navigationWheel.getLayeredPane().add(closeButton);
            step = step + 2 * Math.PI / files.length;
        }

        if (needCodeAnalysis) {
            runCodeAnalysis(manager, fileButtons);
            needCodeAnalysis = Boolean.FALSE;
        }

        userMouseListener.setFileButtons(fileButtons);
        if (navigationWheel.getMouseListeners().length != 0) {
            navigationWheel.removeMouseListener(navigationWheel.getMouseListeners()[0]);
            navigationWheel.removeMouseMotionListener(navigationWheel.getMouseMotionListeners()[0]);
        }
        navigationWheel.addMouseListener(userMouseListener);
        navigationWheel.addMouseMotionListener(userMouseListener);
        navigationWheel.setBackground(navigationWheel);
        SwingUtilities.invokeLater(() -> {
            navigationWheel.setVisible(true);
        });
    }

    private static void runCodeAnalysis(FileEditorManager manager, ArrayList<FileButton> fileButtons) {
        for (FileButton fileButton : fileButtons) {
            List<CodeSmellInfo> codeSmellInfos = CodeSmellDetector.getInstance(manager.getProject()).findCodeSmells(Arrays.asList(fileButton.getVirtualFile()));
            for (CodeSmellInfo codeSmellInfo : codeSmellInfos) {
                if (codeSmellInfo.getSeverity() == HighlightSeverity.ERROR) {
                    String theme = UIManager.getLookAndFeel().getName().toLowerCase();
                    if (theme.contains("darcula") || theme.contains("dark")) {
                        fileButton.setBackground(Color.RED);
                        fileButton.getCloseButton().setBackground(Color.RED);
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