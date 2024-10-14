package plugin.listener;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.action.OpenWheelPlugin;
import plugin.ui.NavigationWheel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static plugin.utils.Constants.REPAINT_WHEEL_ON_FILE_CLOSING_EVENT;

public class CloseButtonListener implements ActionListener {
    private final FileEditorManager fileEditorManager;
    private final Project project;
    private final NavigationWheel wheel;
    private final VirtualFile virtualFile;

    public CloseButtonListener(Project project, NavigationWheel wheel, VirtualFile virtualFile) {
        this.project = project;
        this.fileEditorManager = FileEditorManager.getInstance(project);
        this.wheel = wheel;
        this.virtualFile = virtualFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        closeFileAndDispose();
        if (hasMultipleOpenFiles()) {
            openNewWheel();
        }
    }

    /**
     * Closes the associated file and disposes of the navigation wheel.
     */
    private void closeFileAndDispose() {
        fileEditorManager.closeFile(virtualFile);
        wheel.dispose();
    }

    /**
     * Checks if there are multiple open files in the editor.
     *
     * @return true if there are multiple open files, false otherwise.
     */
    private boolean hasMultipleOpenFiles() {
        return fileEditorManager.getOpenFiles().length > 1;
    }

    /**
     * Opens a new instance of the OpenWheelPlugin.
     */
    private void openNewWheel() {
        DataContext dataContext = createDataContext();

        OpenWheelPlugin openWheelPlugin = new OpenWheelPlugin();
        AnActionEvent event = AnActionEvent.createFromAnAction(
                openWheelPlugin,
                null,
                REPAINT_WHEEL_ON_FILE_CLOSING_EVENT,
                dataContext
        );

        openWheelPlugin.actionPerformed(event);
    }

    /**
     * Creates a DataContext that provides the project for the OpenWheelPlugin.
     *
     * @return a DataContext instance.
     */
    private DataContext createDataContext() {
        return dataId -> {
            if (CommonDataKeys.PROJECT.is(dataId)) {
                return project;
            }
            return null;
        };
    }
}