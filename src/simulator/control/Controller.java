package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

/**
 * Controller class for managing the physics simulator.
 */
public class Controller {

    private final PhysicsSimulator sim;
    private final Factory<Body> bodiesFactory;
    private final Factory<GravityLaws> lawsFactory;

    /**
     * Constructs a Controller with the specified simulator and factories.
     *
     * @param sim           The physics simulator instance.
     * @param bodiesFactory The factory for creating bodies.
     * @param lawsFactory   The factory for creating gravity laws.
     * @throws NullPointerException if any parameter is null.
     */
    public Controller(PhysicsSimulator sim, Factory<Body> bodiesFactory, Factory<GravityLaws> lawsFactory) {
        this.sim = Objects.requireNonNull(sim, "PhysicsSimulator cannot be null.");
        this.bodiesFactory = Objects.requireNonNull(bodiesFactory, "Bodies factory cannot be null.");
        this.lawsFactory = Objects.requireNonNull(lawsFactory, "Gravity laws factory cannot be null.");
    }

    /**
     * Loads bodies into the simulator from an input stream.
     *
     * @param in The input stream containing the bodies' data in JSON format.
     * @throws NullPointerException if the input stream is null.
     */
    public void loadBodies(InputStream in) {
        Objects.requireNonNull(in, "Input stream cannot be null.");
        JSONObject jsonInput = new JSONObject(new JSONTokener(in));
        JSONArray bodies = jsonInput.getJSONArray("bodies");
        for (int i = 0; i < bodies.length(); i++) {
            sim.addBody(bodiesFactory.createInstance(bodies.getJSONObject(i)));
        }
    }

    /**
     * Runs the simulation for a specified number of steps and writes the output to a stream.
     *
     * @param steps The number of simulation steps.
     * @param out   The output stream where the simulation states will be written.
     * @throws IllegalArgumentException if steps is negative.
     */
    public void run(int steps, OutputStream out) {
        if (steps < 0) {
            throw new IllegalArgumentException("Steps cannot be negative.");
        }

        try (PrintStream p = (out == null) ? null : new PrintStream(out)) {
            StringBuilder output = new StringBuilder();
            output.append("{\r\n\"states\": [\r\n");
            output.append(sim.toString());

            for (int i = 0; i < steps; i++) {
                sim.advance();
                output.append(",\r\n").append(sim.toString());
            }

            output.append("\r\n]\r\n}");
            if (p != null) {
                p.print(output.toString());
            }
        }
    }

    /**
     * Resets the simulator to its initial state.
     */
    public void reset() {
        sim.reset();
    }

    /**
     * Sets the delta time for the simulator.
     *
     * @param dt The new delta time.
     * @throws IllegalArgumentException if delta time is non-positive.
     */
    public void setDeltaTime(double dt) {
        if (dt <= 0) {
            throw new IllegalArgumentException("Delta time must be positive.");
        }
        sim.setDeltaTime(dt);
    }

    /**
     * Adds an observer to the simulator.
     *
     * @param o The observer to add.
     * @throws NullPointerException if the observer is null.
     */
    public void addObserver(SimulatorObserver o) {
        Objects.requireNonNull(o, "Observer cannot be null.");
        sim.addObserver(o);
    }

    /**
     * Runs the simulation for a specified number of steps without writing output.
     *
     * @param steps The number of simulation steps.
     * @throws IllegalArgumentException if steps is negative.
     */
    public void run(int steps) {
        if (steps < 0) {
            throw new IllegalArgumentException("Steps cannot be negative.");
        }
        for (int i = 0; i < steps; i++) {
            sim.advance();
        }
    }

    /**
     * Returns the gravity laws factory.
     *
     * @return The gravity laws factory.
     */
    public Factory<GravityLaws> getGravityLawsFactory() {
        return lawsFactory;
    }

    /**
     * Sets the gravity laws for the simulator.
     *
     * @param info The JSON object containing the gravity laws' information.
     * @throws NullPointerException if the info is null.
     * @throws IllegalArgumentException if the gravity laws are invalid.
     */
    public void setGravityLaws(JSONObject info) {
        Objects.requireNonNull(info, "Gravity laws info cannot be null.");
        GravityLaws newLaw = lawsFactory.createInstance(info);
        if (newLaw == null) {
            throw new IllegalArgumentException("Invalid gravity laws: " + info);
        }
        sim.setGravityLaws(newLaw);
    }
}