package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.actions.WheelPlugin;
import plugin.uiComponents.CloseButton;
import plugin.uiComponents.FileButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.ArrayList;

public class UserClicksListener extends Applet implements MouseListener, MouseMotionListener {

    ArrayList<FileButton> fileButtons;
    FileEditorManager fileEditorManager;
    FileButton lastSelected;
    final int X, Y, INNNER_R, OUTER_R, CENTER_X, CENTER_Y;

    public UserClicksListener(int x, int y, int innerR, int outerR, ArrayList<FileButton> fileButtons, FileEditorManager fileEditorManager) {
        this.X = x;
        this.Y = y;
        this.INNNER_R = innerR;
        this.OUTER_R = outerR;
        this.CENTER_X = X + OUTER_R / 2;
        this.CENTER_Y = Y + OUTER_R / 2;
        this.fileButtons = fileButtons;
        this.fileEditorManager = fileEditorManager;
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
        ((JFrame) me.getSource()).dispose();
    }

    // обработать событие наведения курсора мыши
    public void mouseEntered(MouseEvent me) {
        mouseMovedOrDragged(me);
    }

    // обработать событие отведения курсора мыши
    public void mouseExited(MouseEvent me) {

    }

    // обработать событие нажатия кнопки мыши
    public void mousePressed(MouseEvent me) {

    }

    // обработать событие отпускания кнопки мыши
    public void mouseReleased(MouseEvent me) {

    }

    // обработать событие перетаскивания курсора мыши
    public void mouseDragged(MouseEvent me) {
        mouseMovedOrDragged(me);
    }

    // обработать событие перемещения мыши
    public void mouseMoved(MouseEvent me) {
        mouseMovedOrDragged(me);
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

    private void mouseMovedOrDragged(MouseEvent me) {
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
}
