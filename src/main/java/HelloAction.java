import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.*;

public class HelloAction extends AnAction {
    public HelloAction() {
        super("Hello");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        // get opened files
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile files[] = manager.getSelectedFiles();

        ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(new NavigationBar(), null);
        componentPopupBuilder.setMinSize(new Dimension(300, 300));
        JBPopup jbPopup = componentPopupBuilder.createPopup();
        jbPopup.showInBestPositionFor(event.getDataContext());


        //pluginJPanel.setJBPopup(jbPopup);
        Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon());
    }
}