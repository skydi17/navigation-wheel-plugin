package plugin.uiComponents;

import javax.swing.*;

public class PinButton extends JButton {
    PinButtonStatus status;

    PinButton(){
        status = PinButtonStatus.UNPINNED;
    }
}
