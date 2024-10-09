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
    private final int centerX, centerY, paintedRadius, innerRadius;
    private int clickX, clickY;
    private boolean isDragging = false;

    public UserMouseListener(int paintedRadius, Project project, NavigationWheel wheel, int innerRadius) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.centerX = screenSize.width / 2;
        this.centerY = screenSize.height / 2;
        this.paintedRadius = paintedRadius;
        this.innerRadius = innerRadius;
        this.wheel = wheel;
        this.fileEditorManager = FileEditorManager.getInstance(project);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof FileButton) {
            openFileAndCloseWheel();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clickX = e.getX();
        clickY = e.getY();
        isDragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
        Point pointerLocation = MouseInfo.getPointerInfo().getLocation();
        double distance = calculateDistance(pointerLocation.getX(), pointerLocation.getY(), centerX, centerY);

        if (distance < innerRadius && !(e.getSource() instanceof FileButton)) {
            wheel.dispose();
        } else if (lastSelected != null) {
            if (e.getSource() instanceof FileButton) {
                FileButton button = (FileButton) e.getSource();
                if (distance < paintedRadius) {
                    resetButtonPosition(button);
                } else {
                    openFileAndCloseWheel();
                }
            } else {
                openFileAndCloseWheel();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragging && e.getSource() instanceof FileButton) {
            FileButton button = (FileButton) e.getSource();
            dragButton(button, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isDragging && !(e.getSource() instanceof JButton)) {
            highlightClosestButton(e);
        }
    }

    private void openFileAndCloseWheel() {
        if (lastSelected != null) {
            fileEditorManager.openFile(lastSelected.getVirtualFile(), true);
            wheel.dispose();
        }
    }

    private void resetButtonPosition(FileButton button) {
        button.setLocation(button.getOriginalX(), button.getOriginalY());
        button.getCloseButton().setVisible(true);
        button.repaint();
    }

    private void dragButton(FileButton button, int mouseX, int mouseY) {
        button.getCloseButton().setVisible(false);
        int dx = mouseX - clickX;
        int dy = mouseY - clickY;
        button.setLocation(button.getX() + dx, button.getY() + dy);
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void highlightClosestButton(MouseEvent e) {
        Point pointerLocation = MouseInfo.getPointerInfo().getLocation();
        double distance = calculateDistance(pointerLocation.getX(), pointerLocation.getY(), centerX, centerY);

        if (distance < innerRadius) {
            wheel.requestFocus();
            lastSelected = null;
            return;
        }

        FileButton closestButton = fileButtons.get(0);
        double minDistance = Double.MAX_VALUE;

        for (FileButton button : fileButtons) {
            double buttonDistance = calculateDistanceToFileButton(e, button);
            if (buttonDistance < minDistance) {
                closestButton = button;
                minDistance = buttonDistance;
            }
        }

        if (lastSelected != closestButton) {
            closestButton.requestFocus();
            lastSelected = closestButton;
        }
    }

    private double calculateDistanceToFileButton(MouseEvent e, FileButton button) {
        return calculateDistance(e.getX(), e.getY(),
                button.getOriginalX() + button.getWidth() / 2,
                button.getOriginalY() + button.getHeight() / 2);
    }

    public void setFileButtons(ArrayList<FileButton> fileButtons) {
        this.fileButtons = fileButtons;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
