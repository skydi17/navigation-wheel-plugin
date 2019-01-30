package plugin.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import plugin.actions.WheelPlugin;
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
    String msg = "";
    int mouseX = 0, mouseY = 0; // координаты курсора мыши
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

    // обработать событие от щелчка кнопкой мыши
    public void mouseClicked(MouseEvent me) {
        int a = me.getX() - CENTER_X;
        int b = me.getY() - CENTER_Y;
        int l = (int) Math.sqrt(a*a+b*b);
        if (l > INNNER_R && l < OUTER_R) {
            int beginningPointX = CENTER_X + OUTER_R / 2;
            int beginningPointY = CENTER_Y;
            int ab = beginningPointX * me.getY() + beginningPointY * me.getX();
            a = (int) Math.sqrt(beginningPointX * beginningPointX +
                    beginningPointY * beginningPointY);
            b = (int) Math.sqrt(me.getX() * me.getX() +
                    me.getY() * me.getY());
            double alpha = Math.acos(ab / (a * b));
            for (FileButton fileButton : fileButtons) {
                if (fileButton.getFromAngle() < alpha &&
                        fileButton.getToAngle() > alpha) {
                    fileEditorManager.openFile(fileButton.getVirtualFile(), true);
                    fileButton.setBounds(fileButton.getX(), fileButton.getY(),
                            fileButton.getWidth() + 50, fileButton.getHeight() + 50);
                    lastSelected = fileButton;
                    break;
                }
            }
        }
        ((JFrame) me.getSource()).dispose();
    }
    // обработать событие наведения курсора мыши
    public void mouseEntered(MouseEvent me) {

        if (lastSelected != null) {
            lastSelected.setBounds(lastSelected.getX(), lastSelected.getY(),
                    lastSelected.getWidth() - 50, lastSelected.getHeight() - 50);
        }
        int a = me.getX() - CENTER_X;
        int b = me.getY() - CENTER_Y;
        int l = (int) Math.sqrt(a*a+b*b);
        if (l < INNNER_R || l > OUTER_R) {
            lastSelected = null;
        } else {
            int beginningPointX = CENTER_X + OUTER_R / 2;
            int beginningPointY = CENTER_Y;
            int ab = beginningPointX * me.getY() + beginningPointY * me.getX();
            a = (int) Math.sqrt(beginningPointX * beginningPointX +
                    beginningPointY * beginningPointY);
            b = (int) Math.sqrt(me.getX() * me.getX() +
                    me.getY() * me.getY());
            double alpha = Math.acos(ab / (a * b));
            for (FileButton fileButton : fileButtons) {
                if (fileButton.getFromAngle() < alpha &&
                        fileButton.getToAngle() > alpha) {
                    fileButton.setBounds(fileButton.getX(), fileButton.getY(),
                            fileButton.getWidth() + 50, fileButton.getHeight() + 50);
                    lastSelected = fileButton;
                    break;
                }
            }
        }
    }

    // обработать событие отведения курсора мыши
    public void mouseExited(MouseEvent me) {
        // сохранить координаты
        mouseX = 0;
        mouseY = 10;
        msg = "Mouse exited."; // Курсор отведен
        repaint();
    }

    // обработать событие нажатия кнопки мыши
    public void mousePressed(MouseEvent me) {
        // сохранить координаты
        mouseX = me.getX();
        mouseY = me.getY();
        msg = "Down"; // Кнопка мыши нажата
        repaint();
    }

    // обработать событие отпускания кнопки мыши
    public void mouseReleased(MouseEvent me) {
        // сохранить координаты
        mouseX = me.getX();
        mouseY = me.getY();
        msg = "Up"; // Кнопка мыши отпущена
        repaint();
    }

    // обработать событие перетаскивания курсора мыши
    public void mouseDragged(MouseEvent me) {
        // сохранить координаты
        mouseX = me.getX();
        mouseY = me.getY();
        msg = "*";
        //showStatus("Dragging mouse at " + mouseX + ", " + mouseY);
        // Перетаскивание курсора мыши с точку с указанными координатами
        repaint();
    }

    // обработать событие перемещения мыши
    public void mouseMoved(MouseEvent me) {
        // показать состояние
        //showStatus("Moving mouse at " + me.getX() + ", " + me.getY());
        // Перемещение курсора мыши в точку с указанными координатами
    }

    // вывести сообщение из переменной msg на текущенй позиции
    // с координатами X, Y в окне аплета
    public void paint(Graphics g) {
        g.drawString(msg, mouseX, mouseY);
    }

}
