package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.ui.docking.DockManager;
import com.intellij.ui.docking.impl.DockManagerImpl;
import com.intellij.util.ui.UIUtil;
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
    final int INNNER_R, OUTER_R, CENTER_X, CENTER_Y;
    final int CLICK_ACCURACY_DELTA, CHOSEN_BUTTON;
    final int ANIMATION_PAUSE = 10, ANIMATION_LOOP = 5, ANIMATION_SHIFT = 5;
    private int currentX, currentY;
    private boolean dragging = false;

    public UserMouseListener(int x, int y, int innerR, int outerR, Project project, JFrame wheel) {
        if (UIUtil.isRetina()) {
            CLICK_ACCURACY_DELTA = 40;
            CHOSEN_BUTTON = 100;
            this.CENTER_X = wheel.getWidth()/2 - 90;
            this.CENTER_Y = wheel.getHeight()/2 - 60;
        } else {
            CLICK_ACCURACY_DELTA = 20;
            CHOSEN_BUTTON = 50;
            this.CENTER_X = wheel.getWidth()/2 - 45;
            this.CENTER_Y = wheel.getHeight()/2 - 30;
        }
        this.INNNER_R = innerR;
        this.OUTER_R = outerR;
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
        currentX = me.getX();
        currentY = me.getY();
        dragging = true;
    }

    public void mouseReleased(MouseEvent me) {
        dragging = false;
        if (currentX - me.getX() < CLICK_ACCURACY_DELTA && currentY - me.getY() < CLICK_ACCURACY_DELTA) {
            if (lastSelected != null) {
                fileEditorManager.openFile(lastSelected.getVirtualFile(), true);
            }
            wheel.dispose();
            return;
        }
        double l = countLength(me.getX(), me.getY(), CENTER_X, CENTER_Y);
        if (l > OUTER_R / 2) {
            if (lastSelected != null) {
                ((DockManagerImpl)DockManager.getInstance(project)).createNewDockContainerFor(lastSelected.getVirtualFile(), (FileEditorManagerImpl) fileEditorManager);
            }
            wheel.dispose();
        }
    }

    public void mouseDragged(MouseEvent me) {

    }

    public void mouseMoved(MouseEvent me) {
        if (!dragging && !(me.getSource() instanceof JButton)) {
            /*Thread thread = new Thread(){
                public void run(){ */
                    animateFiles(me);
            /*    }
            };

            thread.start(); */
        }

    }

    private double countLength (double fromX, double fromY, double toX, double toY) {
        double a = toX - fromX;
        double b = toY - fromY;
        return Math.sqrt(a*a+b*b);
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
        if (l > INNNER_R && l < OUTER_R) {
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
