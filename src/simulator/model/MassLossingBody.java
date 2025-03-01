package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body {

    private double lossFactor;
    private double lossFrequency;
    private double totalTime;

    public MassLossingBody(String id, Vector velocity, Vector acceleration, Vector position, double mass, double lossFrequency, double lossFactor) {
        super(id, velocity, acceleration, position, mass);
        this.lossFactor = lossFactor;
        this.lossFrequency = lossFrequency;
        this.totalTime = 0.0;
    }

    @Override
    public void move(double time) {
        super.move(time);
        totalTime += time;
        if (totalTime >= lossFrequency) { // Check if lossFrequency seconds have passed
            setMass(getMass() * (1 - lossFactor)); // Reduce mass by lossFactor
            totalTime = 0.0; // Reset the timer
        }
    }
}