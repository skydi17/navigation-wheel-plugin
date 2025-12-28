package plugin.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import plugin.listener.CloseButtonListener;
import plugin.listener.CloseButtonMouseListener;

import javax.swing.*;
import java.awt.*;

public class CloseButton extends JButton {

    private static final int CLOSE_BUTTON_SIZE = 25;
    private final FileButton fileButton;

    public CloseButton(FileButton fileButton, NavigationWheel wheel, Project project) {
        this.fileButton = fileButton;
        setupButton();
        addActionListener(new CloseButtonListener(project, wheel, fileButton.getVirtualFile()));
    }

    public void setupButton() {
        setBounds(fileButton.getX() + fileButton.getWidth(), fileButton.getY(), CLOSE_BUTTON_SIZE, CLOSE_BUTTON_SIZE);
        setMargin(new Insets(2, 2, 0, 0));
        setIcon(AllIcons.Actions.Close);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new CloseButtonMouseListener(this));
    }
}
