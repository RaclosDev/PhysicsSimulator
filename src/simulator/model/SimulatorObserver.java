package simulator.model;

import java.util.List;

public interface SimulatorObserver {

    void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc);

    void onReset(List<Body> bodies, double time, double dt, String gLawsDesc);

    void onBodyAdded(List<Body> bodies, Body b);

    void onAdvance(List<Body> bodies, double time);

    void onDeltaTimeChanged(double dt);

    void onGravityLawChanged(String gLawsDesc);

}