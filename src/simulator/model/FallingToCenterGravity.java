package simulator.model;

import simulator.misc.Vector;

import java.util.List;

public class FallingToCenterGravity implements GravityLaws {

    private static final double GRAVITY = 9.81;

    @Override
    public void apply(List<Body> bodies) {
        Vector center = new Vector(2); // Center of gravity (0, 0)

        for (Body body : bodies) {
            Vector gravityDirection = center.minus(body.getPosition()).direction();
            body.setAcceleration(gravityDirection.scale(GRAVITY));
        }
    }

    @Override
    public String toString() {
        return "Falling to center gravity";
    }
}