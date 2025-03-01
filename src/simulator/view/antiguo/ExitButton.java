

package simulator.view.antiguo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ExitButton extends JButton implements ActionListener {

    public ExitButton() {
        this.setActionCommand("exit");
        this.setIcon(new ImageIcon(this.getClass().getResource("images//exit.png")));
        this.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("exit")) {
            System.exit(0);
        }
    }

}