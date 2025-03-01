package simulator.view;

import org.json.JSONObject;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ControlPanel extends JToolBar implements SimulatorObserver {

    private final Controller controller;
    private JButton openFileButton;
    private JFileChooser fileChooser = new JFileChooser();
    private JButton gravityLawsButton;
    private JButton playButton;
    private JButton stopButton;
    private JButton exitButton;
    private JSpinner stepsSpinner;
    private JTextField deltaTimeField;
    private JSpinner delaySpinner;
    private volatile Thread simulationThread;

    ControlPanel(Controller controller) {
        this.controller = controller;
        initGUI();
        controller.addObserver(this);
    }

    private void initGUI() {
        initOpenFileButton();
        initGravityLawsButton();
        initPlayButton();
        initStopButton();
        initExitButton();
        initStepsSpinner();
        initDeltaTimeField();
        initDelaySpinner();

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(openFileButton);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(gravityLawsButton);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(playButton);
        add(stopButton);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(new JLabel("Delay:"));
        add(delaySpinner);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(new JLabel("Steps:"));
        add(stepsSpinner);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(new JLabel("Delta Time:"));
        add(deltaTimeField);
        add(Box.createRigidArea(new Dimension(450, 0)));
        add(exitButton);
    }

    private void initOpenFileButton() {
        openFileButton = new JButton();
        openFileButton.setActionCommand("open");
        openFileButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("images/open.png"))));
        openFileButton.setToolTipText("Load bodies file into the editor");

        openFileButton.addActionListener(e -> {
            if (e.getActionCommand().equals("open")) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(openFileButton);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("You chose to open this file: " + fileChooser.getSelectedFile().getName());
                }
            }
            try (InputStream in = new FileInputStream(fileChooser.getSelectedFile())) {
                controller.reset();
                controller.loadBodies(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void initGravityLawsButton() {
        gravityLawsButton = new JButton();
        gravityLawsButton.setActionCommand("select");
        gravityLawsButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("images/physics.png"))));
        gravityLawsButton.setToolTipText("Load the gravity law into the editor");

        gravityLawsButton.addActionListener(e -> {
            List<String> lawsArray = new ArrayList<>();
            for (JSONObject o : controller.getGravityLawsFactory().getInfo()) {
                lawsArray.add(o.getString("desc") + " (" + o.get("type") + ")");
            }
            JComboBox<String> lawsComboBox = new JComboBox<>(lawsArray.toArray(new String[0]));

            JFrame frame = new JFrame("Gravity Laws Selector");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel();
            JPanel textPanel = new JPanel();

            buttonPanel.add(createOkButton(frame, lawsComboBox, lawsArray));
            buttonPanel.add(createCancelButton(frame));

            textPanel.add(new JLabel("Select the gravity law to use"));
            textPanel.add(lawsComboBox);

            frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
            frame.getContentPane().add(textPanel, BorderLayout.NORTH);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }

    private JButton createOkButton(JFrame frame, JComboBox<String> lawsComboBox, List<String> lawsArray) {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            frame.dispose();
            String selectedLaw = (String) lawsComboBox.getSelectedItem();
            for (JSONObject o : controller.getGravityLawsFactory().getInfo()) {
                if ((o.getString("desc") + " (" + o.get("type") + ")").equals(selectedLaw)) {
                    controller.setGravityLaws(o);
                }
            }
        });
        return okButton;
    }

    private JButton createCancelButton(JFrame frame) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> frame.dispose());
        return cancelButton;
    }

    private void initPlayButton() {
        playButton = new JButton();
        playButton.setActionCommand("run");
        playButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("images/run.png"))));
        playButton.setToolTipText("Run the simulator");

        playButton.addActionListener(e -> {
            try {
                disableAllButtons();
                simulationThread = new Thread(() -> runSimulation(
                        Integer.parseInt(stepsSpinner.getValue().toString()),
                        ((Integer) delaySpinner.getValue()).longValue()
                ));
                simulationThread.start();
                controller.setDeltaTime(Double.parseDouble(deltaTimeField.getText()));
            } catch (NumberFormatException ex) {
                showErrorDialog("Invalid characters in delta time. Only numbers are allowed.");
            } catch (IllegalArgumentException ex) {
                showErrorDialog(ex.getMessage());
            }
        });
    }

    private void initStopButton() {
        stopButton = new JButton();
        stopButton.setActionCommand("stop");
        stopButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("images/stop.png"))));
        stopButton.setToolTipText("Stop the simulator");

        stopButton.addActionListener(e -> {
            if (simulationThread != null) {
                simulationThread.interrupt();
                simulationThread = null;
                enableAllButtons();
            }
        });
    }

    private void initExitButton() {
        exitButton = new JButton();
        exitButton.setActionCommand("exit");
        exitButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("images/exit.png"))));
        exitButton.setToolTipText("Exit the simulator");

        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to exit the simulator?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void initStepsSpinner() {
        stepsSpinner = new JSpinner(new SpinnerNumberModel(10000, 1, Integer.MAX_VALUE, 1));
        stepsSpinner.setToolTipText("Change the number of simulation steps");
    }

    private void initDeltaTimeField() {
        deltaTimeField = new JTextField("2500");
        deltaTimeField.setToolTipText("Change the delta time");
    }

    private void initDelaySpinner() {
        delaySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        delaySpinner.setToolTipText("Change the delay between steps");
    }

    private void disableAllButtons() {
        openFileButton.setEnabled(false);
        gravityLawsButton.setEnabled(false);
        playButton.setEnabled(false);
        exitButton.setEnabled(false);
        stepsSpinner.setEnabled(false);
        deltaTimeField.setEnabled(false);
    }

    private void enableAllButtons() {
        openFileButton.setEnabled(true);
        gravityLawsButton.setEnabled(true);
        playButton.setEnabled(true);
        exitButton.setEnabled(true);
        stepsSpinner.setEnabled(true);
        deltaTimeField.setEnabled(true);
    }

    private void runSimulation(int steps, long delay) {
        while (steps > 0 && simulationThread != null) {
            try {
                controller.run(1);
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                break;
            } catch (Exception ex) {
                showErrorDialog(ex.getMessage());
                enableAllButtons();
                return;
            }
            steps--;
        }
        enableAllButtons();
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
        SwingUtilities.invokeLater(() -> deltaTimeField.setText(Double.toString(dt)));
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
        SwingUtilities.invokeLater(() -> deltaTimeField.setText(Double.toString(dt)));
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        SwingUtilities.invokeLater(() -> {
            if (bodies.isEmpty()) {
                showErrorDialog("No bodies found. Please load a file.");
            }
        });
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        SwingUtilities.invokeLater(() -> deltaTimeField.setText(Double.toString(dt)));
    }

    @Override
    public void onGravityLawChanged(String gLawsDesc) {
    }
}