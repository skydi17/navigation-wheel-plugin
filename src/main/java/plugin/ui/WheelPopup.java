package plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.WindowManager;
import plugin.listener.ConfigureWindowOnPopupShownListener;
import plugin.listener.LostFocusWindowListener;

import javax.swing.*;
import java.awt.*;

public class WheelPopup {

    public static void create(Project project, NavigationWheel wheel, Rectangle screenBounds) {
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
                .addListener(new ConfigureWindowOnPopupShownListener())
                .createPopup();

        wheel.setPopup(popup);

        showPopup(project, popup, screenBounds);

        Window window = SwingUtilities.getWindowAncestor(wheel);
        if (window != null) {
            window.addWindowFocusListener(new LostFocusWindowListener(wheel));
            configureWindow(window);
            SwingUtilities.invokeLater(() -> window.setOpacity(1f));
        }

    }

    public static void configureWindow(Window window) {
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

    private static void showPopup(Project project, JBPopup popup, Rectangle screenBounds) {
        Window activeWindow = WindowManager.getInstance().getFrame(project);
        if (activeWindow != null) {
            popup.showInScreenCoordinates(activeWindow,
                    new Point(screenBounds.x + screenBounds.width / 2,
                            screenBounds.y + screenBounds.height / 2));
        } else {
            popup.showInFocusCenter();
        }
    }
}
