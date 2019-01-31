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
import plugin.listener.CloseButtonListener;
import plugin.listener.UserClicksListener;
import plugin.uiComponents.CloseButton;
import plugin.uiComponents.FileButton;
import plugin.uiComponents.NavigationBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WheelPlugin extends AnAction {
    public WheelPlugin() {
        super("Open");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        JFrame window = new JFrame();
        window.setTitle("Navigation Wheel");
        window.setUndecorated(true);
        window.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        window.setLocationRelativeTo(WindowManager.getInstance().getFrame(project).getJMenuBar());
        Dimension size = new Dimension();
        size.setSize(400, 400);
        window.setMinimumSize(size);
        window.setMaximumSize(size);
        window.setSize(800, 800);
        viewBar(project, window);
    }

    public void viewBar(Project project, JFrame window) {
        // get opened files
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile files[] = manager.getOpenFiles();

        /*ComponentPopupBuilder componentPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(new NavigationBar(files, project), null);
        componentPopupBuilder.setMinSize(new Dimension(600, 600));
        //componentPopupBuilder.setBorderColor(Color.DARK_GRAY);
        JBPopup jbPopup = componentPopupBuilder.createPopup();
        jbPopup.setCaption("Wheel Navigation");
        WindowManager.getInstance().getIdeFrame(project);
        final JBPopupFactory popupFactory = JBPopupFactory.getInstance();
        final RelativePoint point = popupFactory.guessBestPopupLocation(event.getDataContext());
        jbPopup.show(point); */
        int x = 67, y = 60, w = 400;
        //g.drawOval(x, y, w, w);
        double l = 0;
        ArrayList<FileButton> fileButtons = new ArrayList<>(files.length);
        CloseButtonListener closeButtonListener = new CloseButtonListener(manager);

        //window.getGraphics().drawOval(x, y, w, w);
        for (int i = 0; i < files.length; i++) {
            FileButton file = new FileButton(files[i], x + w/2 + w/2*Math.cos(l), y + w/2 + w/2*Math.sin(l));
            file.setText(files[i].getName());
            file.setBounds((int)(x + w/2 + w/2*Math.cos(l - Math.PI/files.length) - 50),
                    (int) (y + w/2 + w/2*Math.sin(l - Math.PI/files.length) + 10), 125, 25);
            file.setEnabled(true);
            file.setVisible(true);
            file.setFocusable(Boolean.FALSE);
            file.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    manager.openFile(file.getVirtualFile(), Boolean.TRUE);
                    window.dispose();
                }
            });

            window.add(file);
            fileButtons.add(file);
            CloseButton closeButton = new CloseButton(files[i]);
            closeButton.setBounds(file.getX() + file.getWidth(), file.getY(), 20, 25);
            closeButton.setEnabled(true);
            closeButton.setVisible(true);
            closeButton.setText("x");
            closeButton.setFocusable(Boolean.FALSE);
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    manager.closeFile(closeButton.getVirtualFile());
                    window.getContentPane().removeAll();
                    window.getContentPane().repaint();
                    viewBar(project, window);
                }
            });
            closeButton.addActionListener(closeButtonListener);
            window.add(closeButton);
            //g.drawLine(x + w/2, y + w/2,
            //        (int)(x + w/2 + w/2*Math.cos(l)), (int) (y + w/2 + w/2*Math.sin(l)));
            l = l + 2 * Math.PI/files.length;
        }
        window.setSize(600, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setVisible(true);
        UserClicksListener userClicksListener = new UserClicksListener(x, y, 50, w, fileButtons, manager);
        window.addMouseListener(userClicksListener);
        window.addMouseMotionListener(userClicksListener);
    }
}