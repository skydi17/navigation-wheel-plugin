package plugin.utils;

import com.intellij.openapi.diagnostic.Logger;
import plugin.action.OpenWheelPlugin;

import java.awt.*;

public class MouseCursorCenterMover {

    private static final Logger logger = Logger.getInstance(OpenWheelPlugin.class);

    public static void centerMouseCursor() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;
        try {
            Robot robot = new Robot();
            robot.mouseMove(centerX, centerY);
        } catch (AWTException e) {
            logger.warn("Mouse cursor wasn't moved to the center due to an error", e);
        }
    }

}
