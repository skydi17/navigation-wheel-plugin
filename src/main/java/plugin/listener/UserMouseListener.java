package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.ui.docking.DockManager;
import com.intellij.ui.docking.impl.DockManagerImpl;
import javafx.animation.Timeline;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class UserMouseListener extends Applet implements MouseListener, MouseMotionListener {

    private ArrayList<FileButton> fileButtons;
    private final FileEditorManager fileEditorManager;
    private FileButton lastSelected;
    private final Project project;
    private final JFrame wheel;
    final int DIAMETER, CENTER_X, CENTER_Y;
    final int WHEEL_ACCURACY_DELTA = 80, WIDTH_ACCURACY_DELTA = 45, HEIGHT__ACCURACY_DELTA = 30;
    private int clickX, clickY;
    private boolean isButtonDragging = Boolean.FALSE;

    public UserMouseListener(int diameter, Project project, JFrame wheel) {
        this.CENTER_X = wheel.getWidth()/2 - WIDTH_ACCURACY_DELTA;
        this.CENTER_Y = wheel.getHeight()/2 - HEIGHT__ACCURACY_DELTA;
        this.DIAMETER = diameter;
        this.project = project;
        this.wheel = wheel;
        this.fileEditorManager = FileEditorManager.getInstance(project);
    }

    public void mouseClicked(MouseEvent me) {

    }

    public void mouseEntered(MouseEvent me) {

    }

    public void mouseExited(MouseEvent me) {

    }

    public void mousePressed(MouseEvent me) {
        clickX = me.getX();
        clickY = me.getY();
        isButtonDragging = Boolean.TRUE;
    }

    public void mouseReleased(MouseEvent me) {
        isButtonDragging = Boolean.FALSE;
        if (lastSelected != null) {
            fileEditorManager.openFile(lastSelected.getVirtualFile(), Boolean.TRUE);
        }
        wheel.dispose();
    }

    public void mouseDragged(MouseEvent me) {
        if (isButtonDragging && me.getSource() instanceof FileButton) {
            Point info = MouseInfo.getPointerInfo().getLocation();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double l = countLength(info.getX(), info.getY(),
                    screenSize.getWidth()/2, screenSize.getHeight()/2);
            FileButton button = (FileButton) me.getSource();
            if (l < DIAMETER/2 + WHEEL_ACCURACY_DELTA) {
                button.getCloseButton().setVisible(Boolean.FALSE);
                int dx = me.getX() - clickX;
                int dy = me.getY() - clickY;
                button.setLocation(button.getX() + dx, button.getY() + dy);
            } else {
                ((DockManagerImpl)DockManager.getInstance(project)).createNewDockContainerFor(button.getVirtualFile(), (FileEditorManagerImpl) fileEditorManager);
                wheel.dispose();
            }
        }
    }

    public void mouseMoved(MouseEvent me) {
        if (!isButtonDragging && !(me.getSource() instanceof JButton)) {
            animateFiles(me);
        }
    }

    private double countLength (double fromX, double fromY, double toX, double toY) {
        double a = toX - fromX;
        double b = toY - fromY;
        return Math.sqrt(a*a + b*b);
    }

    private void animateFiles(MouseEvent me) {
        double min = Double.MAX_VALUE;
        FileButton closestButton = fileButtons.get(0);
        for (FileButton fileButton : fileButtons) {
            double l = countLength(me.getX(), me.getY(),
                    fileButton.getOriginalX() + fileButton.getWidth() / 2,
                    fileButton.getOriginalY() + fileButton.getHeight() / 2);
            if (l < min) {
                closestButton = fileButton;
                min = l;
            }
        }
        if (lastSelected != closestButton) {
            closestButton.requestFocus();
            lastSelected = closestButton;
        }
    }

    public ArrayList<FileButton> getFileButtons() {
        return fileButtons;
    }

    public void setFileButtons(ArrayList<FileButton> fileButtons) {
        this.fileButtons = fileButtons;
    }
}
