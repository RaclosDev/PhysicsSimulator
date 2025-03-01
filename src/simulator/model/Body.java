package simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import simulator.misc.Vector;

@AllArgsConstructor
@Data
public class Body {

    private String id;
    private Vector velocity;
    private Vector acceleration;
    private Vector position;
    private double mass;

    public void move(double time) {
        position = position.plus(velocity.scale(time)).plus(acceleration.scale(0.5 * Math.pow(time, 2)));
        velocity = velocity.plus(acceleration.scale(time));
    }

    @Override
    public String toString() {
        return String.format(
                "{ \"id\": \"%s\", \"mass\": %f, \"pos\": %s, \"vel\": %s, \"acc\": %s }",
                id, mass, position, velocity, acceleration
        );
    }
}