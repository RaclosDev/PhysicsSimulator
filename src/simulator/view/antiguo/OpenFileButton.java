package simulator.view.antiguo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OpenFileButton extends JButton implements ActionListener {
    JFileChooser chooser = new JFileChooser();

    public OpenFileButton() {
        this.setActionCommand("abrir");
        this.setIcon(new ImageIcon(this.getClass().getResource("images//open.png")));
        this.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("abrir")) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter( "TXT", "txt");
                    chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getName());
            }

        }
    }

}
