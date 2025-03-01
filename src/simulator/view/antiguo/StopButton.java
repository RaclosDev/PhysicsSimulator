

package simulator.view.antiguo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StopButton extends JButton implements ActionListener {

    public StopButton() {
        this.setActionCommand("abrir");
        this.setIcon(new ImageIcon(this.getClass().getResource("images//stop.png")));
        this.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("abrir")) {
        }
    }

}