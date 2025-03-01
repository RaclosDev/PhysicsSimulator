package simulator.view.antiguo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PlayButton extends JButton implements ActionListener {

    public PlayButton() {
        this.setActionCommand("run");
        this.setIcon(new ImageIcon(this.getClass().getResource("images//run.png")));
        this.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("run")) {

        }
    }

}