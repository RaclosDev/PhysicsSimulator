package simulator.view;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatusBar extends JPanel implements SimulatorObserver {

    private JLabel currentTimeLabel;
    private JLabel currentLawsLabel;
    private JLabel numberOfBodiesLabel;

    StatusBar(Controller controller) {
        initGUI();
        controller.addObserver(this);
    }

    private void initGUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createBevelBorder(1));

        currentTimeLabel = new JLabel("Time: 0");
        numberOfBodiesLabel = new JLabel("Bodies: 0");
        currentLawsLabel = new JLabel("Law: Select a law");

        add(currentTimeLabel);
        add(numberOfBodiesLabel);
        add(currentLawsLabel);
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String gravityLawsDesc) {
        updateLabels(bodies, time, gravityLawsDesc);
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String gravityLawsDesc) {
        updateLabels(bodies, time, gravityLawsDesc);
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body body) {
        SwingUtilities.invokeLater(() -> numberOfBodiesLabel.setText("Bodies: " + bodies.size()));
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        SwingUtilities.invokeLater(() -> currentTimeLabel.setText("Time: " + time));
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        // No es necesario actualizar la barra de estado
    }

    @Override
    public void onGravityLawChanged(String gravityLawsDesc) {
        SwingUtilities.invokeLater(() -> currentLawsLabel.setText("Law: " + gravityLawsDesc));
    }

    private void updateLabels(List<Body> bodies, double time, String gravityLawsDesc) {
        SwingUtilities.invokeLater(() -> {
            currentTimeLabel.setText("Time: " + time);
            numberOfBodiesLabel.setText("Bodies: " + bodies.size());
            currentLawsLabel.setText("Law: " + gravityLawsDesc);
        });
    }
}