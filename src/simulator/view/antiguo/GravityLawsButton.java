package simulator.view.antiguo;


import org.json.JSONObject;
import simulator.control.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GravityLawsButton extends JButton implements ActionListener {

    Controller _ctrl;

    public GravityLawsButton(Controller _ctrl) {
        this._ctrl = _ctrl;
        this.setActionCommand("seleccionar");
        this.setIcon(new ImageIcon(this.getClass().getResource("images//physics.png")));
        this.addActionListener(this);
    }

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    String[] lawsArray = new String[]{"Newton’s law of universal gravitation", "Falling to  center gravity"};
    JComboBox<String> lawsJComboBox = new JComboBox<>(lawsArray);


    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame("Gravity Laws Selector");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JPanel textPanel = new JPanel();

        buttonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        textPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(textPanel, BorderLayout.NORTH);


        buttonPanel.add(createOkButton(frame, lawsJComboBox));
        buttonPanel.add(createCancelButton(frame));

        textPanel.add(new JLabel("Select the gravity law to use"));
        textPanel.add(lawsJComboBox);
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
        frame.setResizable(false);




    }

    public JButton createOkButton(JFrame frame, JComboBox lawsJComboBox) {
        JButton okButton = new JButton("ok");
        okButton.setActionCommand("ok");
        okButton.setVisible(true);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                String selectedLaw = (String) lawsJComboBox.getSelectedItem();

                List<String> options = new ArrayList<>();
                for (JSONObject o : _ctrl.getGravityLawsFactory().getInfo()) {
                    options.add(o.getString("desc") + " ( " + o.get("type") + " ) ");
                }
                for (String s : options){

                }

                System.out.println("You seleted the Law: " + selectedLaw);

                //TODO FALTA HACER QUE CAMBIE LA LEY SOLO LA MUESTRA

            }
        });


        return okButton;
    }

    public JButton createCancelButton(JFrame frame) {
        JButton cancelButton = new JButton("cancel");//added for cancel button
        cancelButton.setActionCommand("cancel");
        cancelButton.setVisible(true);//added for cancel button
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

            }
        });

        return cancelButton;
    }


}
