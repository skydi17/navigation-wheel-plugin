package plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class OpenWheelWithCodeAnalysis extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        OpenWheelPlugin plugin = new OpenWheelPlugin(true);
        plugin.actionPerformed(event);
    }
}
