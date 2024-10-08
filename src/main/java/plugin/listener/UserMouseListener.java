package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import plugin.ui.FileButton;
import plugin.ui.NavigationWheel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class UserMouseListener implements MouseListener, MouseMotionListener {

    private ArrayList<FileButton> fileButtons;
    private final FileEditorManager fileEditorManager;
    private FileButton lastSelected;
    private final NavigationWheel wheel;
    final int DIAMETER, CENTER_X, CENTER_Y, PAINTED_R, INNER_R;
    private int clickX, clickY;
    private boolean isButtonDragging = Boolean.FALSE;

    public UserMouseListener(int diameter, int paintedR, Project project, NavigationWheel wheel, int innerR) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.CENTER_X = screenSize.width / 2;
        this.CENTER_Y = screenSize.height / 2;
        this.DIAMETER = diameter;
        this.PAINTED_R = paintedR;
        this.INNER_R = innerR;
        this.wheel = wheel;
        this.fileEditorManager = FileEditorManager.getInstance(project);
    }

    public void mouseClicked(MouseEvent me) {
        if (me.getSource() instanceof FileButton) {
            fileEditorManager.openFile(lastSelected.getVirtualFile(), Boolean.TRUE);
            wheel.dispose();
        }
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
        Point info = MouseInfo.getPointerInfo().getLocation();
        double l = countLength(info.getX(), info.getY(), CENTER_X, CENTER_Y);
        if (l < INNER_R && !(me.getSource() instanceof FileButton)) {
            wheel.dispose();
            return;
        }
        if (lastSelected != null) {
            if (me.getSource() instanceof FileButton) {
                FileButton button = (FileButton) me.getSource();
                if (l < PAINTED_R) {
                    button.setLocation(button.getOriginalX(), button.getOriginalY());
                    button.getCloseButton().setVisible(true);
                    button.repaint();
                } else {
                    fileEditorManager.openFile(lastSelected.getVirtualFile(), Boolean.TRUE);
                    wheel.dispose();
                }
            } else {
                fileEditorManager.openFile(lastSelected.getVirtualFile(), Boolean.TRUE);
                wheel.dispose();
            }
        }
    }

    public void mouseDragged(MouseEvent me) {
        if (isButtonDragging && me.getSource() instanceof FileButton) {
            FileButton button = (FileButton) me.getSource();
            button.getCloseButton().setVisible(Boolean.FALSE);
            int dx = me.getX() - clickX;
            int dy = me.getY() - clickY;
            button.setLocation(button.getX() + dx, button.getY() + dy);
        }
    }

    public void mouseMoved(MouseEvent me) {
        if (!isButtonDragging && !(me.getSource() instanceof JButton)) {
            animateFiles(me);
        }
    }

    private double countLength(double fromX, double fromY, double toX, double toY) {
        double a = toX - fromX;
        double b = toY - fromY;
        return Math.sqrt(a * a + b * b);
    }

    private void animateFiles(MouseEvent me) {
        Point info = MouseInfo.getPointerInfo().getLocation();
        double distance = countLength(info.getX(), info.getY(), CENTER_X, CENTER_Y);
        if (distance < INNER_R) {
            wheel.requestFocus();
            lastSelected = null;
            return;
        }
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
