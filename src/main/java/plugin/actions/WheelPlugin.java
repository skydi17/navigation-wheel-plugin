package plugin.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import src.com.plugin.uiComponents.NavigationBar;

import java.awt.*;

public class WheelPlugin extends AnAction {
    public WheelPlugin() {
        super("Open");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        // get opened files
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile files[] = manager.getOpenFiles();

        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(new NavigationBar(files, project), null);
        componentPopupBuilder.setMinSize(new Dimension(600, 600));
        //componentPopupBuilder.setBorderColor(Color.DARK_GRAY);
        JBPopup jbPopup = componentPopupBuilder.createPopup();
        jbPopup.setCaption("Wheel Navigation");
        WindowManager.getInstance().getIdeFrame(project);
        final JBPopupFactory popupFactory = JBPopupFactory.getInstance();
        final RelativePoint point = popupFactory.guessBestPopupLocation(event.getDataContext());
        jbPopup.show(point);

        //Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon());
    }
}