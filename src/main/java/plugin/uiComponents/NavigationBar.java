package plugin.uiComponents;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.actions.CloseFile;
import plugin.actions.OpenFile;
import plugin.listener.UserClicksListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NavigationBar extends JComponent {

    private enum Actions {
        OPEN,
        CLOSE
    }

    VirtualFile[] files;
    Project project;
    List pinnedFiles = new ArrayList<VirtualFile>(20);

    public NavigationBar(VirtualFile[] files, Project project) {
        this.files = files;
    }

    /*public void paint(Graphics g) {
        int x = 10, y = 10, w = 500;
        g.drawOval(x, y, w, w);
        double l = 0;
        JFrame window = new JFrame();
        OpenFile openFile = new OpenFile();
        //UserClicksListener userClicksListener = new UserClicksListener(x,y, 50, w, null);
        ArrayList<FileButton> fileButtons = new ArrayList<>(files.length);

        //window.getGraphics().drawOval(x, y, w, w);
        for (int i = 0; i < files.length; i++) {
            FileButton file = new FileButton(x + w/2 + w/2*Math.cos(l), y + w/2 + w/2*Math.sin(l));
            file.setText(files[i].getName());
            file.setBounds((int)(x + w/2 + w/2*Math.cos(l - Math.PI/files.length) - 50),
                            (int) (y + w/2 + w/2*Math.sin(l - Math.PI/files.length) + 10), 100, 20);
            file.setEnabled(true);
            file.setVisible(true);
            //file.setActionCommand(Actions.OPEN.name());
            //file.addActionListener(openFile);

            window.add(file);
            fileButtons.add(file);
            CloseButton closeButton = new CloseButton();
            closeButton.setBounds(file.getX() + file.getWidth(), file.getY(), 20, 20);
            closeButton.setEnabled(true);
            closeButton.setVisible(true);
            //closeButton.setActionCommand(Actions.CLOSE.name());
            window.add(closeButton);
            g.drawLine(x + w/2, y + w/2,
                    (int)(x + w/2 + w/2*Math.cos(l)), (int) (y + w/2 + w/2*Math.sin(l)));
            l = l + 2 * Math.PI/files.length;
        }
        window.setSize(600, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setVisible(true);
    } */

    private void pin(VirtualFile file) {
        if (pinnedFiles.size() == 20) {
            Messages.showMessageDialog(project, "Cannot pin more files. Max bar size is 20.", "Warning", Messages.getInformationIcon());
            return;
        }
        pinnedFiles.add(file);
    }
}