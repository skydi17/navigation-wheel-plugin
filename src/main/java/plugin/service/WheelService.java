package plugin.service;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import plugin.listener.LostFocusWindowListener;
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

        Window activeWindow = WindowManager.getInstance().getFrame(project);
        Rectangle screenBounds = activeWindow != null
                ? activeWindow.getGraphicsConfiguration().getBounds()
                : GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        Dimension screenSize = new Dimension(screenBounds.width, screenBounds.height);
        NavigationWheel wheel = new NavigationWheel(
                screenSize.height,
                screenSize.width
        );

        setupFileButtons(project, wheel, screenSize, screenBounds);
    }

    private static void setupFileButtons(
            Project project,
            NavigationWheel wheel,
            Dimension screenSize,
            Rectangle screenBounds
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

        UserMouseListener mouseListener = new UserMouseListener(
                PAINTED_R,
                project,
                wheel,
                INNER_R,
                buttons
        );

        configureWheel(project, wheel, mouseListener, screenBounds, buttons);
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

    private static void configureWindow(Window window) {
        if (window == null) return;
        window.setOpacity(0f);
        window.setBackground(new Color(0, 0, 0, 0));
        if (window instanceof RootPaneContainer rpc) {
            rpc.getContentPane().setBackground(new Color(0, 0, 0, 0));
            if (rpc.getContentPane() instanceof JComponent jc) {
                jc.setOpaque(false);
            }
        }
        window.revalidate();
        window.repaint();
    }

    private static void configureWheel(
            Project project,
            NavigationWheel wheel,
            UserMouseListener listener,
            Rectangle screenBounds,
            List<FileButton> buttons
    ) {
        wheel.addMouseListener(listener);
        wheel.addMouseMotionListener(listener);
        for (FileButton button : buttons) {
            button.addMouseListener(listener);
            button.addMouseMotionListener(listener);
        }
        wheel.setBackgroundImage();

        JBPopup popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(wheel, wheel)
                .setCancelOnClickOutside(true)
                .setCancelOnWindowDeactivation(true)
                .setCancelKeyEnabled(true)
                .setFocusable(true)
                .setRequestFocus(true)
                .setModalContext(false)
                .setShowBorder(false)
                .setShowShadow(false)
                .setNormalWindowLevel(true)
                .addListener(new JBPopupListener() {
                    @Override
                    public void beforeShown(LightweightWindowEvent event) {
                        Window window = SwingUtilities.getWindowAncestor(event.asPopup().getContent());
                        configureWindow(window);
                    }
                })
                .createPopup();

        wheel.setPopup(popup);

        Window activeWindow = WindowManager.getInstance().getFrame(project);
        if (activeWindow != null) {
            popup.showInScreenCoordinates(activeWindow,
                    new Point(screenBounds.x + screenBounds.width / 2,
                            screenBounds.y + screenBounds.height / 2));
        } else {
            popup.showInFocusCenter();
        }

        Window window = SwingUtilities.getWindowAncestor(wheel);
        if (window != null) {
            window.addWindowFocusListener(new LostFocusWindowListener(wheel));
            configureWindow(window);
            SwingUtilities.invokeLater(() -> window.setOpacity(1f));
        }
    }
}
