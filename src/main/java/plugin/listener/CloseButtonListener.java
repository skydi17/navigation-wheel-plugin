package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;

import java.applet.Applet;
import java.awt.event.*;

public class CloseButtonListener extends Applet implements ActionListener {

    FileEditorManager fileEditorManager;

    public CloseButtonListener(FileEditorManager fileEditorManager) {
        this.fileEditorManager = fileEditorManager;

    }

    public void actionPerformed(ActionEvent e) {
    }
}
