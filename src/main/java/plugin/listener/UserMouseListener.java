package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import plugin.ui.CloseButton;
import plugin.ui.FileButton;
import plugin.ui.NavigationWheel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class UserMouseListener implements MouseListener, MouseMotionListener {

    private final List<FileButton> fileButtons;
    private final FileEditorManager fileEditorManager;
    private FileButton lastSelected;
    private final NavigationWheel wheel;
    private final int centerX, centerY, paintedRadius, innerRadius;

    public UserMouseListener(int paintedRadius,
                             Project project,
                             NavigationWheel wheel,
                             int innerRadius,
                             List<FileButton> fileButtons) {

        this.wheel = wheel;
        this.fileEditorManager = FileEditorManager.getInstance(project);
        this.fileButtons = fileButtons;
        this.paintedRadius = paintedRadius;
        this.innerRadius = innerRadius;
        this.centerX = wheel.getWidth() / 2;
        this.centerY = wheel.getHeight() / 2;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof FileButton) {
            lastSelected = (FileButton) e.getSource();
            openFileAndCloseWheel();
        } else {
            Point p = getRelativePoint(e);
            double distance = calculateDistance(p.x, p.y, centerX, centerY);
            if (distance > paintedRadius || distance < innerRadius) {
                wheel.dispose();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = getRelativePoint(e);
        double distance = calculateDistance(p.x, p.y, centerX, centerY);

        if (distance > paintedRadius) {
            wheel.dispose();
            return;
        }

        if (distance < innerRadius && !(e.getSource() instanceof FileButton)) {
            wheel.dispose();
        } else if (lastSelected != null) {
            if (e.getSource() instanceof FileButton button) {
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getSource() instanceof CloseButton) {
            if (lastSelected != null) {
                wheel.requestFocusInWindow();
                lastSelected = null;
            }
            return;
        }
        Point p = getRelativePoint(e);
        highlightClosestButton(p.x, p.y);
    }

    private Point getRelativePoint(MouseEvent e) {
        Point p = e.getPoint();
        if (e.getSource() != wheel && e.getSource() instanceof Component comp) {
            p = SwingUtilities.convertPoint(comp, p, wheel);
        }
        return p;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (lastSelected != null) {
            wheel.requestFocusInWindow();
            lastSelected = null;
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

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void highlightClosestButton(int mouseX, int mouseY) {
        double distance = calculateDistance(mouseX, mouseY, centerX, centerY);

        if (distance < innerRadius || distance > paintedRadius) {
            if (lastSelected != null) {
                wheel.requestFocusInWindow();
                lastSelected = null;
            }
            return;
        }

        FileButton closestButton = fileButtons.get(0);
        double minDistance = Double.MAX_VALUE;

        for (FileButton button : fileButtons) {
            double buttonDistance = calculateDistanceToFileButton(mouseX, mouseY, button);
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

    private double calculateDistanceToFileButton(int mouseX, int mouseY, FileButton button) {
        return calculateDistance(mouseX, mouseY,
                button.getOriginalX() + (double) button.getWidth() / 2,
                button.getOriginalY() + (double) button.getHeight() / 2);
    }
}
