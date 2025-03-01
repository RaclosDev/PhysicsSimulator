package simulator.view;


import org.json.JSONObject;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends JToolBar implements SimulatorObserver {

    private Controller _ctrl;
    //private boolean _stopped;
    JButton openFileButton;
    JFileChooser chooser = new JFileChooser();
    JButton gravityLawsButton;
    JButton playButton;
    JButton stopButton;
    JButton exitButton;
    JSpinner selectorPasos;
    JTextField changeDeltaTime;
    JSpinner delayTimeSpinner;
    private volatile Thread _thread;



    ControlPanel(Controller ctrl) {

        _ctrl = ctrl;
        //_stopped = true;
        initGUI();
        _ctrl.addObserver(this);
    }

    private void initGUI() {
        iniOpenFileButton();
        iniOpenLawSelectorButton();
        iniPlayButton();
        iniStopButton();
        iniExitButton();
        iniSelectorPasos();
        iniChangeDeltaTime();
        iniDelaySpinner();

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        this.add(openFileButton);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(gravityLawsButton);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(playButton);
        this.add(stopButton);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(new JLabel("Delay:"));
        this.add(delayTimeSpinner);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(new JLabel("Steps:"));
        this.add(selectorPasos);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(new JLabel("Delta Time:"));
        this.add(changeDeltaTime);
        this.add(Box.createRigidArea(new Dimension(450, 0)));
        this.add(exitButton);
    }

    private void iniOpenFileButton() {
        openFileButton = new JButton();
        openFileButton.setActionCommand("abrir");
        openFileButton.setIcon(new ImageIcon(this.getClass().getResource("images//open.png")));
        openFileButton.setToolTipText("Load bodies file into the editor");

        openFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("abrir")) {
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(openFileButton);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
                    }

                }
                InputStream in = null;
                try {
                    in = new FileInputStream(chooser.getSelectedFile());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                _ctrl.reset();
                _ctrl.loadBodies(in);
            }
        });
    }

    private void iniOpenLawSelectorButton() {
        gravityLawsButton = new JButton();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        gravityLawsButton.setActionCommand("seleccionar");
        gravityLawsButton.setIcon(new ImageIcon(this.getClass().getResource("images//physics.png")));
        gravityLawsButton.setToolTipText("Load the gravity law into the editor");

        gravityLawsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> lawsArray = new ArrayList<>();

                for (JSONObject o : _ctrl.getGravityLawsFactory().getInfo()) {
                    lawsArray.add(o.getString("desc") + " (" + o.get("type") + ")");
                }
                JComboBox<String> lawsJComboBox = new JComboBox<>();
                lawsJComboBox.setModel(new DefaultComboBoxModel(lawsArray.toArray()));


                JFrame frame = new JFrame("Gravity Laws Selector");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                frame.getContentPane().setLayout(new BorderLayout());

                JPanel buttonPanel = new JPanel();
                JPanel textPanel = new JPanel();

                buttonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                textPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
                frame.getContentPane().add(textPanel, BorderLayout.NORTH);


                buttonPanel.add(createOkButton(frame, lawsJComboBox, lawsArray));
                buttonPanel.add(createCancelButton(frame));

                textPanel.add(new JLabel("Select the gravity law to use"));
                textPanel.add(lawsJComboBox);
                frame.pack();
                frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
                frame.setVisible(true);
                frame.setResizable(false);
            }
        });
    }

    private JButton createOkButton(JFrame frame, JComboBox lawsJComboBox, List lawsArrays) {
        JButton okButton = new JButton("ok");
        okButton.setActionCommand("ok");
        okButton.setVisible(true);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                String selectedLaw = (String) lawsJComboBox.getSelectedItem();
                for (JSONObject o : _ctrl.getGravityLawsFactory().getInfo()) {

                    if ((o.getString("desc") + " (" + o.get("type") + ")").equals(selectedLaw)) {
                        _ctrl.setGravityLaws(o);
                    }
                }


                //TODO FALTA HACER QUE CAMBIE LA LEY SOLO LA MUESTRA

            }
        });


        return okButton;
    }

    private JButton createCancelButton(JFrame frame) {
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

    private void iniPlayButton() throws IllegalArgumentException {
        playButton = new JButton();

        playButton.setActionCommand("run");
        playButton.setIcon(new ImageIcon(this.getClass().getResource("images//run.png")));
        playButton.setToolTipText("Run the simulator");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("run")) {
                    try {
                        disableAllButtons();

                        _thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                run_sim(Integer.parseInt(selectorPasos.getValue().toString()),((Integer) delayTimeSpinner.getValue()).longValue());
                            }
                        });

                        _thread.start();
                        _ctrl.setDeltaTime(Double.parseDouble(changeDeltaTime.getText()));

                        //_stopped = false;
                        //run_sim(Integer.parseInt(selectorPasos.getValue().toString()));
                    } catch (NumberFormatException ex2) { //Para caracteres no permitidos
                        JFrame frame = new JFrame("JOptionPane showMessageDialog error");
                        JOptionPane.showMessageDialog(frame, "Hay caracteres no permitidos en delta time, solo se aceptan numeros.");
                    } catch (IllegalArgumentException ex) { //Para numeros diferentes
                        JFrame frame = new JFrame("JOptionPane showMessageDialog error");
                        JOptionPane.showMessageDialog(frame, ex.getMessage());
                    }

                    //_ctrl.setDeltaTime(Integer.parseInt(changeDeltaTime.getText()));

                }
            }
        });

    }

    private void iniStopButton() {
        stopButton = new JButton();

        stopButton.setActionCommand("stop");
        stopButton.setIcon(new ImageIcon(this.getClass().getResource("images//stop.png")));
        stopButton.setToolTipText("Stop the simulator");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("stop")) {
                    _thread.interrupt();
                    _thread = null;
                    //_stopped = true;
                    enableAllButtons();
                }
            }
        });

    }

    private void iniExitButton() {
        exitButton = new JButton();

        exitButton.setActionCommand("exit");
        exitButton.setIcon(new ImageIcon(this.getClass().getResource("images//exit.png")));
        exitButton.setToolTipText("Exit the simulator");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("exit")) {

                    JFrame frame = new JFrame("JOptionPane showMessageDialog error");
                    Object[] opciones = {"Aceptar", "Cancelar"};
                    int eleccion = JOptionPane.showOptionDialog(frame, "¿Seguro que quieres cerrar el simulador?", "Mensaje de Confirmacion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
                    if (eleccion == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            }
        });

    }

    private void iniChangeDeltaTime() {
        changeDeltaTime = new JTextField("2500");
        changeDeltaTime.setToolTipText("Change the delta time");

    }

    private void iniSelectorPasos() {
        selectorPasos = new JSpinner();
        selectorPasos.setValue(10000);
        selectorPasos.setToolTipText("Cambia los pasos de la simulacion");
    }

    private void disableAllButtons() {
        openFileButton.setEnabled(false);
        gravityLawsButton.setEnabled(false);
        playButton.setEnabled(false);
        exitButton.setEnabled(false);
        selectorPasos.setEnabled(false);
        changeDeltaTime.setEnabled(false);
    }

    private void enableAllButtons() {
        openFileButton.setEnabled(true);
        gravityLawsButton.setEnabled(true);
        playButton.setEnabled(true);
        exitButton.setEnabled(true);
        selectorPasos.setEnabled(true);
        changeDeltaTime.setEnabled(true);
    }

    private void iniDelaySpinner() {
        SpinnerNumberModel delay = new SpinnerNumberModel(0, 0, 1000, 1);
        delayTimeSpinner = new JSpinner(delay);
        delayTimeSpinner.setToolTipText("Cambia el delay entre pasos");
    }

    private void run_sim(int n, long delay) {
        while (n > 0 && _thread != null) {
            try {
                this._ctrl.run(1);

            } catch (Exception e) {
                JFrame frame = new JFrame("JOptionPane showMessageDialog error");
                JOptionPane.showMessageDialog(frame, e.getMessage());
                //this._stopped=true;
                enableAllButtons();
                return;
            }
            try {
                _thread.sleep(delay);
            } catch (InterruptedException ex) {

            } catch (Exception e) {
                e.printStackTrace();
            }
            n--;
        }

        //this._stopped = true;
        exitButton.setEnabled(true);

        /*
        if (n > 0 && !_stopped) {
            try {
                _ctrl.run(1);
            } catch (Exception e) {
                // TODO Muestra el errorcon una ventana JOptionPane
                JFrame frame = new JFrame("JOptionPane showMessageDialog error");
                JOptionPane.showMessageDialog(frame, e.getMessage());
                // TODO Pon enabletodos los botones
                enableAllButtons();
                _stopped = true;
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    run_sim(n - 1);
                }
            });
        }
        else {
            _stopped = true;
            //TODO Pon enable todos los botone
            enableAllButtons();
        }
        */
    }


    //TODO Añade los métodos de SimulatorObserver
    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                changeDeltaTime.setText(Double.toString(dt));
            }
        });

        //changeDeltaTime.setText(Double.toString(dt));
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                changeDeltaTime.setText(Double.toString(dt));
            }
        });


        //changeDeltaTime.setText(Double.toString(dt));
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {

    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (bodies.isEmpty()) {
                    throw new IllegalArgumentException("No hay ningun cuerpo, selecciona un fichero");
                }
            }
        });



        /*if (bodies.isEmpty()) {
            throw new IllegalArgumentException("No hay ningun cuerpo, selecciona un fichero");
        }*/
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                changeDeltaTime.setText(Double.toString(dt));
            }
        });

        //changeDeltaTime.setText(Double.toString(dt));
    }

    @Override
    public void onGravityLawChanged(String gLawsDesc) {

    }


}