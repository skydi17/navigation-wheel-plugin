package plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.listener.UserMouseListener;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;
import plugin.ui.NavigationWheel;
import plugin.utils.MouseCursorCenterMover;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static plugin.utils.Constants.REPAINT_WHEEL_ON_FILE_CLOSING_EVENT;

public class OpenWheelPlugin extends AnAction {

    private final Logger logger = Logger.getInstance(OpenWheelPlugin.class);

    private static final int X = 5;
    private static final int Y = 70;
    private static final int D = 430;
    private static final int PAINTED_R = 295;
    private static final int INNER_R = 60;
    private static final String NOT_ENOUGH_FILES_MESSAGE = "Not enough files opened.";
    private static final String TITLE_MESSAGE = "Information";

    private NavigationWheel navigationWheel;
    private int wheelHeight;
    private int wheelWidth;

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) return;

        FileEditorManager manager = FileEditorManager.getInstance(project);
        if (manager.getOpenFiles().length <= 1) {
            showNotEnoughFilesMessage(project);
            return;
        }

        setUpScreenSize();
        createWheel(project);

        if (!REPAINT_WHEEL_ON_FILE_CLOSING_EVENT.equals(event.getPlace())) {
            MouseCursorCenterMover.centerMouseCursor();
        }
    }

    private void showNotEnoughFilesMessage(Project project) {
        Messages.showMessageDialog(project, NOT_ENOUGH_FILES_MESSAGE, TITLE_MESSAGE, Messages.getInformationIcon());
    }

    private void setUpScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.wheelHeight = screenSize.height;
        this.wheelWidth = screenSize.width;
    }

    private void createWheel(Project project) {
        navigationWheel = new NavigationWheel(wheelHeight, wheelWidth);
        setUpFileButtons(project);
    }

    private void setUpFileButtons(Project project) {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile[] files = manager.getOpenFiles();

        if (files.length == 0) return;

        List<FileButton> fileButtons = createFileButtons(files, project);

        UserMouseListener userMouseListener = new UserMouseListener(
                PAINTED_R, project, navigationWheel, INNER_R, fileButtons);

        configureNavigationWheel(userMouseListener);
    }

    private List<FileButton> createFileButtons(VirtualFile[] files, Project project) {
        List<FileButton> fileButtons = new ArrayList<>(files.length);
        double step = 0;

        for (VirtualFile file : files) {
            FileButton fileButton = createFileButton(file, step, project, files.length);
            fileButtons.add(fileButton);
            step += 2 * Math.PI / files.length;
        }

        return fileButtons;
    }

    private FileButton createFileButton(VirtualFile file, double step, Project project, int totalFiles) {
        FileButton fileButton = new FileButton(file, step, totalFiles, D / 2,
                wheelWidth / 2 - PAINTED_R + X, wheelHeight / 2 - PAINTED_R + Y);

        CloseButton closeButton = new CloseButton(fileButton, navigationWheel, project);
        closeButton.setupButton();
        fileButton.setCloseButton(closeButton);

        navigationWheel.getLayeredPane().add(fileButton);
        navigationWheel.getLayeredPane().add(closeButton);

        return fileButton;
    }

    private void configureNavigationWheel(UserMouseListener userMouseListener) {
        removeExistingListeners();
        navigationWheel.addMouseListener(userMouseListener);
        navigationWheel.addMouseMotionListener(userMouseListener);
        navigationWheel.setBackgroundImage();

        SwingUtilities.invokeLater(() -> navigationWheel.setVisible(true));
    }

    private void removeExistingListeners() {
        if (navigationWheel.getMouseListeners().length > 0) {
            navigationWheel.removeMouseListener(navigationWheel.getMouseListeners()[0]);
        }
        if (navigationWheel.getMouseMotionListeners().length > 0) {
            navigationWheel.removeMouseMotionListener(navigationWheel.getMouseMotionListeners()[0]);
        }
    }

}
