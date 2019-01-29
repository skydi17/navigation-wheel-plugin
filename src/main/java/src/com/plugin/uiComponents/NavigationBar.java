package src.com.plugin.uiComponents;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBBox;
import src.com.plugin.listener.UserClicksListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NavigationBar extends JComponent {

    VirtualFile[] files;
    Project project;
    List pinnedFiles = new ArrayList<VirtualFile>(20);

    public NavigationBar(VirtualFile[] files, Project project) {
        this.files = files;
    }

    public void paint(Graphics g) {
        int x = 10, y = 10, w = 500;
        g.drawOval(x, y, w, w);
        double l = 0;
        for (int i = 0; i < files.length; i++) {
            g.drawString(files[i].getName(),
                    (int)(x + w/2 + w/2*Math.cos(l)), (int) (y + w/2 + w/2*Math.sin(l)));
            g.drawLine(x + w/2, y + w/2,
                    (int)(x + w/2 + w/2*Math.cos(l)), (int) (y + w/2 + w/2*Math.sin(l)));
            l = l + 2 * Math.PI/files.length;
        }
        UserClicksListener userClicksListener = new UserClicksListener();
        userClicksListener.init();
    }

    private void pin(VirtualFile file) {
        if (pinnedFiles.size() == 20) {
            Messages.showMessageDialog(project, "Cannot pin more files. Max bar size is 20.", "Warning", Messages.getInformationIcon());
            return;
        }
        pinnedFiles.add(file);
    }

    private void close(VirtualFile file) {

    }
}