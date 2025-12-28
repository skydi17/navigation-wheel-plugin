package plugin.listener;

import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static plugin.ui.WheelPopup.configureWindow;

public class ConfigureWindowOnPopupShownListener implements JBPopupListener {

    @Override
    public void beforeShown(@NotNull LightweightWindowEvent event) {
        Window window = SwingUtilities.getWindowAncestor(
                event.asPopup().getContent()
        );
        configureWindow(window);
    }
}
