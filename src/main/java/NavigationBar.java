import javax.swing.*;
import java.awt.*;

public class NavigationBar extends JComponent {
    public void paint(Graphics g) {
        int x = 150, y = 100, w = 75;
        g.drawOval(x, y, w, w);
        for (int i = 0; i < 19; i++) {
            g.drawLine(x + w/2, y + w/2, );
        }

    }
}