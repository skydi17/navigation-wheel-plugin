package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.ui.docking.DockManager;
import com.intellij.ui.docking.impl.DockManagerImpl;
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

    ArrayList<FileButton> fileButtons;
    FileEditorManager fileEditorManager;
    FileButton lastSelected;
    Project project;
    JFrame wheel;
    final int D, CENTER_X, CENTER_Y;
    final int WHEEL_ACCURACY_DELTA = 80, CHOSEN_BUTTON = 50;
    final int ANIMATION_PAUSE = 10, ANIMATION_LOOP = 5, ANIMATION_SHIFT = 5;
    private int clickX, clickY;
    private boolean isButtonDragging = false;

    public UserMouseListener(int d, Project project, JFrame wheel) {
        this.CENTER_X = wheel.getWidth()/2 - 45;
        this.CENTER_Y = wheel.getHeight()/2 - 30;
        //this.INNNER_R = innerR;
        this.D = d;
        this.project = project;
        this.wheel = wheel;
        this.fileEditorManager = FileEditorManager.getInstance(project);
    }

    public void mouseClicked(MouseEvent me) {
        /*if (!dragging) {
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
        } */
    }

    public void mouseEntered(MouseEvent me) {

    }

    public void mouseExited(MouseEvent me) {

    }

    public void mousePressed(MouseEvent me) {
        clickX = me.getX();
        clickY = me.getY();
        isButtonDragging = true;
    }

    public void mouseReleased(MouseEvent me) {
        isButtonDragging = false;
        if (lastSelected != null) {
            fileEditorManager.openFile(lastSelected.getVirtualFile(), true);
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
            if (l < D/2 + WHEEL_ACCURACY_DELTA) {
                button.getCloseButton().setVisible(false);
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

    private void increaseButton(FileButton button) {
        button.setBounds(button.getX(), button.getY(),
                button.getWidth() + CHOSEN_BUTTON, button.getHeight() + CHOSEN_BUTTON);
        CloseButton closeButton = button.getCloseButton();
        closeButton.setBounds(button.getX() + button.getWidth(), button.getY(),
                closeButton.getWidth() + CHOSEN_BUTTON, closeButton.getHeight() + CHOSEN_BUTTON);
    }

    private void decreaseButton(FileButton button) {
        button.setBounds(button.getX(), button.getY(),
                button.getWidth() - CHOSEN_BUTTON, button.getHeight() - CHOSEN_BUTTON);
        CloseButton closeButton = button.getCloseButton();
        closeButton.setBounds(button.getX() + button.getWidth(), button.getY(),
                closeButton.getWidth() - CHOSEN_BUTTON, closeButton.getHeight() - CHOSEN_BUTTON);
    }

    private void pullFileToTheCentre(FileButton button) {
        for (int i = 0; i < ANIMATION_LOOP; i++) {
            double length = countLength(button.getX(), button.getY(), CENTER_X, CENTER_Y);
            double invLength = -1/length*ANIMATION_SHIFT;
            button.setLocation( button.getX() - (int)((double)(CENTER_X - button.getX())*invLength),
                    button.getY() - (int)((double)(CENTER_Y - button.getY())*invLength));
            button.repaint();
            button.getCloseButton().setLocation(button.getX() + button.getWidth(), button.getY());
            button.getCloseButton().repaint();
            /*try {
                Thread.sleep(ANIMATION_PAUSE);
            } catch (InterruptedException e) {

            } */
        }
    }

    private void pullFileOutOfCentre(FileButton button) {
        for (int i = 0; i < ANIMATION_LOOP; i++) {
            double length = countLength(CENTER_X, CENTER_Y, button.getX(), button.getY());
            double invLength = 1/length*ANIMATION_SHIFT;
            button.setLocation( button.getX() - (int)((double)(CENTER_X - button.getX())*invLength),
                    button.getY() - (int)((double)(CENTER_Y - button.getY())*invLength));
            button.repaint();
            button.getCloseButton().setLocation(button.getX() + button.getWidth(), button.getY());
            button.getCloseButton().repaint();
            /*try {
                Thread.sleep(ANIMATION_PAUSE);
            } catch (InterruptedException e) {

            }*/
        }
    }

    private void animateFiles(MouseEvent me) {
        double l = countLength(me.getX(), me.getY(), CENTER_X, CENTER_Y);
        if (l < D) {
            double min = Double.MAX_VALUE;
            FileButton closestButton = fileButtons.get(0);
            for (FileButton fileButton : fileButtons) {
                l = countLength(me.getX(), me.getY(),
                        fileButton.getOriginalX() + fileButton.getWidth()/2,
                        fileButton.getOriginalY() + fileButton.getHeight()/2);
                if (l < min) {
                    closestButton = fileButton;
                    min = l;
                }
            }
            if (lastSelected != closestButton) {
                if (lastSelected != null){
                    pullFileOutOfCentre(lastSelected);
                }
                pullFileToTheCentre(closestButton);
                lastSelected = closestButton;
            }
        } else {
            if (lastSelected != null) {
                pullFileOutOfCentre(lastSelected);
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
