package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {

    private GravityLaws gravityLaws;   //leyes de la gravedad a aplicar.
    private List<Body> bodies;         //cuerpos de la simulación;
    private double dt;                 //incremento del tiempo. TIEMPO REAL POR PASO
    private double time;               //número de pasos que se ejecuta la simulación.
    private List<SimulatorObserver> simulatorObservers;

    public PhysicsSimulator(GravityLaws gravityLaws, double dt) {
        this.gravityLaws = gravityLaws;
        this.dt = dt;
        time = 0;
        bodies = new ArrayList<>();
        simulatorObservers = new ArrayList<>();
    }

    public void addBody(Body b) {//que añade b a bodies.
        boolean newBody = true;
        for (Body body : bodies) {
            if (body.getId().equals(b.getId())) {
                newBody = false;
                break;
            }
        }

        if (newBody == true) {
            bodies.add(b);
        }

        for (SimulatorObserver o : simulatorObservers) {
            o.onBodyAdded(bodies, b);
        }

    }

    public void advance() {
        gravityLaws.apply(bodies);
        //NewtonUniversalGravitation n = new NewtonUniversalGravitation();
        // n.apply(bodies);

        for (Body bodies : bodies) {
            bodies.move(dt);
        }
        time += dt;

        for (SimulatorObserver o : simulatorObservers) {
            o.onAdvance(bodies, time);
        }
    }

    public String toString() {
        String infoBodies;

        infoBodies = "{ \"time\": " + time + ", \"bodies\": [";

        for (int i = 0; i < bodies.size(); i++) {
            infoBodies += "" + bodies.get(i).toString();
            if (i < bodies.size() - 1) {
                infoBodies += ", ";
            }

        }
        infoBodies += "] }";

        return infoBodies;
    }

    public void reset() {
        bodies = new ArrayList<>();
        time = 0;
        for (SimulatorObserver o : simulatorObservers) {
            o.onReset(bodies, time, dt, gravityLaws.toString());
        }

    }

    public void setDeltaTime(double dt) throws IllegalArgumentException {
        if (dt <= 0) {
            throw new IllegalArgumentException("Delta time no puede ser <=0");
        } else if (Double.toString(dt) == "0") {
            throw new IllegalArgumentException("Delta time no puede ser un string");
        } else {
            this.dt = dt;
            for (SimulatorObserver o : simulatorObservers) {
                o.onDeltaTimeChanged(dt);
            }
        }
    }

    public void setGravityLaws(GravityLaws gravityLaws) throws IllegalArgumentException {

        if (gravityLaws == null) {
            throw new IllegalArgumentException("Gravity Law == null");
        } else {
            this.gravityLaws = gravityLaws;
            for (SimulatorObserver o : simulatorObservers) {
                o.onGravityLawChanged(gravityLaws.toString());
            }
        }
    }

    public void addObserver(SimulatorObserver o) {
        simulatorObservers.add(o);
        o.onRegister(bodies, time, dt, gravityLaws.toString());
    }

}
