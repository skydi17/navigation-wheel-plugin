package plugin.service;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import plugin.listener.UserMouseListener;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;
import plugin.ui.NavigationWheel;
import plugin.ui.WheelPopup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static plugin.utils.Constants.*;

public final class WheelService {

    private WheelService() {
    }

    public static void openWheel(Project project) {
        FileEditorManager fem = FileEditorManager.getInstance(project);

        if (fem.getOpenFiles().length <= 1) {
            Messages.showMessageDialog(
                    project,
                    NOT_ENOUGH_FILES_MESSAGE,
                    TITLE_MESSAGE,
                    Messages.getInformationIcon()
            );
            return;
        }

        Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        if (activeWindow == null) {
            activeWindow = WindowManager.getInstance().getFrame(project);
        }

        Rectangle screenBounds;
        if (activeWindow != null && activeWindow.getGraphicsConfiguration() != null) {
            screenBounds = activeWindow.getGraphicsConfiguration().getBounds();
        } else {
            screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        }

        Dimension screenSize = new Dimension(screenBounds.width, screenBounds.height);
        NavigationWheel wheel = new NavigationWheel(
                screenSize.height,
                screenSize.width
        );

        setupFileButtons(project, wheel, screenSize);
        configureWheel(project, wheel, screenBounds);
    }

    public static void refreshWheel(Project project, NavigationWheel wheel) {
        FileEditorManager fem = FileEditorManager.getInstance(project);

        if (fem.getOpenFiles().length <= 1) {
            wheel.dispose();
            return;
        }

        Window window = SwingUtilities.getWindowAncestor(wheel);
        Rectangle screenBounds;
        if (window != null && window.isShowing() && window.getGraphicsConfiguration() != null) {
            screenBounds = window.getGraphicsConfiguration().getBounds();
        } else {
            Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
            if (activeWindow == null) {
                activeWindow = WindowManager.getInstance().getFrame(project);
            }

            if (activeWindow != null && activeWindow.getGraphicsConfiguration() != null) {
                screenBounds = activeWindow.getGraphicsConfiguration().getBounds();
            } else {
                screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            }
        }

        Dimension screenSize = new Dimension(screenBounds.width, screenBounds.height);
        wheel.updateDimensions(screenSize.height, screenSize.width);

        wheel.clearButtons();

        setupFileButtons(project, wheel, screenSize);

        if (window != null && window.isShowing()) {
            window.setOpacity(1f);
            window.setBounds(screenBounds);
            window.setLocation(screenBounds.x, screenBounds.y);
            window.setSize(screenBounds.width, screenBounds.height);
            window.revalidate();
            window.repaint();
        } else {
            configureWheel(project, wheel, screenBounds);
        }

        updateWheelListeners(project, wheel);
        wheel.setBackgroundImage();

        wheel.requestFocusInWindow();
        wheel.revalidate();
        wheel.repaint();
    }

    private static void setupFileButtons(
            Project project,
            NavigationWheel wheel,
            Dimension screenSize
    ) {
        FileEditorManager fem = FileEditorManager.getInstance(project);

        Set<VirtualFile> openFiles = Arrays.stream(fem.getOpenFiles())
                .collect(Collectors.toSet());

        List<VirtualFile> lastOpenFiles = Lists.reverse(EditorHistoryManager.getInstance(project).getFileList())
                .stream()
                .filter(openFiles::contains)
                .limit(10)
                .toList();

        if (lastOpenFiles.isEmpty()) {
            wheel.dispose();
            return;
        }

        List<FileButton> buttons = createFileButtons(
                lastOpenFiles,
                project,
                wheel,
                screenSize
        );

        wheel.putClientProperty("fileButtons", buttons);
    }

    private static void updateWheelListeners(Project project, NavigationWheel wheel) {
        @SuppressWarnings("unchecked")
        List<FileButton> buttons = (List<FileButton>) wheel.getClientProperty("fileButtons");

        for (MouseListener ml : wheel.getMouseListeners()) {
            if (ml instanceof UserMouseListener) {
                wheel.removeMouseListener(ml);
            }
        }
        for (MouseMotionListener mml : wheel.getMouseMotionListeners()) {
            if (mml instanceof UserMouseListener) {
                wheel.removeMouseMotionListener(mml);
            }
        }

        UserMouseListener mouseListener = new UserMouseListener(
                PAINTED_R,
                project,
                wheel,
                INNER_R,
                buttons
        );

        wheel.addMouseListener(mouseListener);
        wheel.addMouseMotionListener(mouseListener);
        for (FileButton button : buttons) {
            button.addMouseListener(mouseListener);
            button.addMouseMotionListener(mouseListener);
            if (button.getCloseButton() != null) {
                button.getCloseButton().addMouseListener(mouseListener);
                button.getCloseButton().addMouseMotionListener(mouseListener);
            }
        }
    }

    private static List<FileButton> createFileButtons(
            List<VirtualFile> files,
            Project project,
            NavigationWheel wheel,
            Dimension screenSize
    ) {
        List<FileButton> result = new ArrayList<>(files.size());
        double step = 0;

        for (VirtualFile file : files) {
            FileButton fileButton = new FileButton(
                    file,
                    step,
                    D / 2,
                    screenSize.width / 2 - PAINTED_R + X,
                    screenSize.height / 2 - PAINTED_R + Y,
                    FileEditorManager.getInstance(project),
                    wheel
            );

            CloseButton closeButton = new CloseButton(
                    fileButton,
                    wheel,
                    project
            );
            fileButton.setCloseButton(closeButton);

            wheel.add(fileButton);
            wheel.add(closeButton);

            result.add(fileButton);
            step += 2 * Math.PI / files.size();
        }

        return result;
    }

    private static void configureWheel(
            Project project,
            NavigationWheel wheel,
            Rectangle screenBounds
    ) {
        updateWheelListeners(project, wheel);
        wheel.setBackgroundImage();

        WheelPopup.create(project, wheel, screenBounds);
    }
}
