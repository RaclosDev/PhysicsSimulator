package simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhysicsSimulator {

    private GravityLaws gravityLaws; // Gravity laws to apply
    private List<Body> bodies;      // Bodies in the simulation
    private double deltaTime;       // Real time per step
    private double time;            // Total simulation time
    private List<SimulatorObserver> observers; // Observers

    public PhysicsSimulator(GravityLaws gravityLaws, double deltaTime) {
        this.gravityLaws = Objects.requireNonNull(gravityLaws, "Gravity laws cannot be null");
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("Delta time must be positive");
        }
        this.deltaTime = deltaTime;
        this.time = 0;
        this.bodies = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void addBody(Body body) {
        Objects.requireNonNull(body, "Body cannot be null");

        boolean isNewBody = bodies.stream().noneMatch(b -> b.getId().equals(body.getId()));
        if (isNewBody) {
            bodies.add(body);
            observers.forEach(o -> o.onBodyAdded(bodies, body));
        }
    }

    public void advance() {
        gravityLaws.apply(bodies);
        bodies.forEach(b -> b.move(deltaTime));
        time += deltaTime;
        observers.forEach(o -> o.onAdvance(bodies, time));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"time\": ").append(time).append(", \"bodies\": [");
        for (int i = 0; i < bodies.size(); i++) {
            sb.append(bodies.get(i).toString());
            if (i < bodies.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] }");
        return sb.toString();
    }

    public void reset() {
        bodies.clear();
        time = 0;
        observers.forEach(o -> o.onReset(bodies, time, deltaTime, gravityLaws.toString()));
    }

    public void setDeltaTime(double deltaTime) {
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("Delta time must be positive");
        }
        this.deltaTime = deltaTime;
        observers.forEach(o -> o.onDeltaTimeChanged(deltaTime));
    }

    public void setGravityLaws(GravityLaws gravityLaws) {
        this.gravityLaws = Objects.requireNonNull(gravityLaws, "Gravity laws cannot be null");
        observers.forEach(o -> o.onGravityLawChanged(gravityLaws.toString()));
    }

    public void addObserver(SimulatorObserver observer) {
        Objects.requireNonNull(observer, "Observer cannot be null");
        observers.add(observer);
        observer.onRegister(bodies, time, deltaTime, gravityLaws.toString());
    }
}