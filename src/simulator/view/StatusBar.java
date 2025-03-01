package simulator.view;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatusBar extends JPanel implements SimulatorObserver {
    // ...
    private JLabel _currTime; // for current time
    private JLabel _currLaws; // for gravity laws
    private JLabel _numOfBodies; // for number of bodies

    StatusBar(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {


        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createBevelBorder(1));
        _currTime = new JLabel("Time: 0");
        _numOfBodies = new JLabel("Bodies: 0");
        _currLaws = new JLabel("Law: Select a law");


        this.add(_currTime);
        this.add(_numOfBodies);
        this.add(_currLaws);

// TODO complete the code to build the tool bar
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {

    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {

    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _numOfBodies.setText("Bodies: " + bodies.size());
            }
        });
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _currTime.setText("Time: " + time);
            }
        });

    }

    @Override
    public void onDeltaTimeChanged(double dt) {
    }

    @Override
    public void onGravityLawChanged(String gLawsDesc) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _currLaws.setText(gLawsDesc);
                System.out.println("Law: " + gLawsDesc);
            }
        });
    }
}
