package plugin.action;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
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
import java.util.Set;
import java.util.stream.Collectors;

import static plugin.utils.Constants.*;

public class OpenWheelPlugin extends AnAction {

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
        navigationWheel = new NavigationWheel(project, wheelHeight, wheelWidth);
        setUpFileButtons(project);
    }

    private void setUpFileButtons(Project project) {
        FileEditorManager fem = FileEditorManager.getInstance(project);

        Set<VirtualFile> openFiles = Arrays.stream(fem.getOpenFiles())
                .collect(Collectors.toSet());

        List<VirtualFile> last10Open =
                Lists.reverse(EditorHistoryManager.getInstance(project).getFileList())
                        .stream()
                        .filter(openFiles::contains)
                        .limit(10)
                        .toList();

        if (last10Open.isEmpty()) return;

        List<FileButton> fileButtons = createFileButtons(last10Open, project);
        UserMouseListener userMouseListener = new UserMouseListener(
                PAINTED_R, project, navigationWheel, INNER_R, fileButtons);

        configureNavigationWheel(userMouseListener);
    }

    private List<FileButton> createFileButtons(List<VirtualFile> files, Project project) {
        List<FileButton> fileButtons = new ArrayList<>(files.size());
        double step = 0;

        for (VirtualFile file : files) {
            FileButton fileButton = createFileButton(file, step, project);
            fileButtons.add(fileButton);
            step += 2 * Math.PI / files.size();
        }

        return fileButtons;
    }

    private FileButton createFileButton(VirtualFile file, double step, Project project) {
        FileButton fileButton = new FileButton(file, step, D / 2,
                wheelWidth / 2 - PAINTED_R + X, wheelHeight / 2 - PAINTED_R + Y,
                FileEditorManager.getInstance(project), navigationWheel);

        CloseButton closeButton = new CloseButton(fileButton, navigationWheel, project);
        closeButton.setupButton();
        fileButton.setCloseButton(closeButton);

        navigationWheel.getLayeredPane().add(fileButton);
        navigationWheel.getLayeredPane().add(closeButton);

        return fileButton;
    }

    private void configureNavigationWheel(UserMouseListener userMouseListener) {
        navigationWheel.addMouseListener(userMouseListener);
        navigationWheel.addMouseMotionListener(userMouseListener);
        navigationWheel.setBackgroundImage();

        SwingUtilities.invokeLater(() -> navigationWheel.setVisible(true));
    }

}
