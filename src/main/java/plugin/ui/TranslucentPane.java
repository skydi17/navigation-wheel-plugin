package plugin.ui;

import javax.swing.*;
import java.awt.*;

public class TranslucentPane extends JPanel {

    public TranslucentPane() {
        setOpaque(Boolean.FALSE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.SrcOver.derive(0.0f));
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
