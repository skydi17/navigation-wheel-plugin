package plugin.listener;

import plugin.ui.CloseButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CloseButtonMouseListener extends MouseAdapter {

    private final CloseButton button;

    public CloseButtonMouseListener(CloseButton button) {
        this.button = button;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        button.requestFocus();
    }

}
