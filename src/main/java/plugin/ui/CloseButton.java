package plugin.ui;

import com.intellij.openapi.project.Project;
import plugin.listener.CloseButtonListener;

import javax.swing.*;

public class CloseButton extends JButton {

    private final int CLOSE_BUTTON_SIZE;
    private FileButton fileButton;

    public CloseButton(FileButton fileButton) {
        CLOSE_BUTTON_SIZE = 25;
        this.fileButton = fileButton;
    }

    public FileButton getFileButton() {
        return fileButton;
    }

    public void setFileButton(FileButton fileButton) {
        this.fileButton = fileButton;
    }

    public CloseButton createCloseButton(final JFrame wheel,
                                         Project project) {
        CloseButton closeButton = this;
        closeButton.setBounds(fileButton.getX() + fileButton.getWidth(), fileButton.getY(),
                CLOSE_BUTTON_SIZE, CLOSE_BUTTON_SIZE);
        closeButton.setEnabled(Boolean.TRUE);
        closeButton.setVisible(Boolean.TRUE);
        closeButton.setText("x");
        closeButton.setFocusable(Boolean.FALSE);
        closeButton.addActionListener(new CloseButtonListener(project,
                wheel, closeButton.getFileButton().getVirtualFile()));
        return closeButton;
    }
}
