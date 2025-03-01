package simulator.model;

import simulator.misc.Vector;

import java.util.List;

public class NewtonUniversalGravitation implements GravityLaws {

    private static final double GRAVITATIONAL_CONSTANT = 6.67E-11; // Gravitational constant

    private Vector calculateForce(Body a, Body b) {
        Vector direction = b.getPosition().minus(a.getPosition());
        double distanceSquared = direction.dot(direction);
        double forceMagnitude = (GRAVITATIONAL_CONSTANT * a.getMass() * b.getMass()) / distanceSquared;
        return direction.direction().scale(forceMagnitude);
    }

    @Override
    public void apply(List<Body> bodies) {
        for (Body body : bodies) {
            Vector totalForce = new Vector(2); // Initialize to zero vector
            if (body.getMass() > 0) {
                for (Body otherBody : bodies) {
                    if (!body.equals(otherBody)) {
                        totalForce = totalForce.plus(calculateForce(body, otherBody));
                    }
                }
                body.setAcceleration(totalForce.scale(1 / body.getMass()));
            } else {
                body.setAcceleration(totalForce); // Zero acceleration for massless bodies
            }
        }
    }

    @Override
    public String toString() {
        return "Newton’s law of universal gravitation";
    }
}