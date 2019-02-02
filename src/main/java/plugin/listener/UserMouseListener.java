package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.ui.docking.DockManager;
import com.intellij.ui.docking.impl.DockManagerImpl;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;

import javax.swing.*;
import java.awt.event.*;
import java.applet.*;
import java.util.ArrayList;

public class UserMouseListener extends Applet implements MouseListener, MouseMotionListener {

    ArrayList<FileButton> fileButtons;
    FileEditorManager fileEditorManager;
    FileButton lastSelected;
    Project project;
    JFrame wheel;
    final int X, Y, INNNER_R, OUTER_R, CENTER_X, CENTER_Y;
    private int currentX, currentY;
    private boolean dragging = false;

    public UserMouseListener(int x, int y, int innerR, int outerR, Project project, JFrame wheel) {
        this.X = x;
        this.Y = y;
        this.INNNER_R = innerR;
        this.OUTER_R = outerR;
        this.CENTER_X = X + OUTER_R / 2;
        this.CENTER_Y = Y + OUTER_R / 2;
        this.project = project;
        this.wheel = wheel;
        this.fileEditorManager = FileEditorManager.getInstance(project);
    }

    public void mouseClicked(MouseEvent me) {
        double l = countDistanceFromMouseCursor(me, CENTER_X, CENTER_Y);
        if (l > INNNER_R && l < OUTER_R) {
            double min = Double.MAX_VALUE;
            FileButton closestButton = fileButtons.get(0);
            for (FileButton fileButton : fileButtons) {
                l = countDistanceFromMouseCursor(me,
                        fileButton.getLocX(), fileButton.getLocY());
                if (l < min) {
                    closestButton = fileButton;
                    min = l;
                }
            }
            fileEditorManager.openFile(closestButton.getVirtualFile(), true);
        }
        wheel.dispose();
    }

    public void mouseEntered(MouseEvent me) {

    }

    public void mouseExited(MouseEvent me) {

    }

    public void mousePressed(MouseEvent me) {
        currentX = me.getX();
        currentY = me.getY();
        dragging = true;
    }

    public void mouseReleased(MouseEvent me) {
        dragging = false;
        if (currentX == me.getX() && currentY == me.getY()) {
            if (lastSelected != null) {
                fileEditorManager.openFile(lastSelected.getVirtualFile(), true);
            }
            wheel.dispose();
            return;
        }
        double l = countDistanceFromMouseCursor(me, CENTER_X, CENTER_Y);
        if (l > OUTER_R / 2) {
            if (lastSelected != null) {
                ((DockManagerImpl)DockManager.getInstance(project)).createNewDockContainerFor(lastSelected.getVirtualFile(), (FileEditorManagerImpl)fileEditorManager);
            }
            wheel.dispose();
        }
    }

    public void mouseDragged(MouseEvent me) {

    }

    public void mouseMoved(MouseEvent me) {
        if (!dragging && !(me.getSource() instanceof JButton)) {
            animateFiles(me);
        }
    }

    private double countDistanceFromMouseCursor(MouseEvent me, double toX, double toY) {
        double a = me.getX() - toX;
        double b = me.getY() - toY;
        return (int) Math.sqrt(a*a+b*b);
    }

    private void increaseButton(FileButton button) {
        button.setBounds(button.getX(), button.getY(),
                button.getWidth() + 50, button.getHeight() + 50);
        CloseButton closeButton = button.getCloseButton();
        closeButton.setBounds(button.getX() + button.getWidth(), button.getY(),
                closeButton.getWidth() + 50, closeButton.getHeight() + 50);
    }

    private void decreaseButton(FileButton button) {
        button.setBounds(button.getX(), button.getY(),
                button.getWidth() - 50, button.getHeight() - 50);
        CloseButton closeButton = button.getCloseButton();
        closeButton.setBounds(button.getX() + button.getWidth(), button.getY(),
                closeButton.getWidth() - 50, closeButton.getHeight() - 50);
    }

    private void animateFiles(MouseEvent me) {
        double l = countDistanceFromMouseCursor(me, CENTER_X, CENTER_Y);
        if (l > INNNER_R && l < OUTER_R) {
            double min = Double.MAX_VALUE;
            FileButton closestButton = fileButtons.get(0);
            for (FileButton fileButton : fileButtons) {
                l = countDistanceFromMouseCursor(me,
                        fileButton.getLocX(), fileButton.getLocY());
                if (l < min) {
                    closestButton = fileButton;
                    min = l;
                }
            }
            if (lastSelected != closestButton) {
                if (lastSelected != null){
                    decreaseButton(lastSelected);
                }
                increaseButton(closestButton);
                lastSelected = closestButton;
            }
        } else {
            if (lastSelected != null) {
                decreaseButton(lastSelected);
                lastSelected = null;
            }
        }
    }

    public ArrayList<FileButton> getFileButtons() {
        return fileButtons;
    }

    public void setFileButtons(ArrayList<FileButton> fileButtons) {
        this.fileButtons = fileButtons;
    }
}
