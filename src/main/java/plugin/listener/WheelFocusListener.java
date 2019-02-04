package plugin.listener;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class WheelFocusListener implements FocusListener {

    JFrame wheel;

    public WheelFocusListener(JFrame wheel) {
        this.wheel = wheel;
    }

    public void focusGained(FocusEvent fe){

    }

    public void focusLost(FocusEvent fe){
        wheel.dispose();
    }
}
