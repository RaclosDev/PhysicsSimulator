package simulator.model;

import simulator.misc.Vector;

import java.util.List;


public class FallingToCenterGravity implements GravityLaws {

    static private final double g = 9.81;

    public FallingToCenterGravity() {
    }

    @Override
    public void apply(List<Body> bodies) {

        Vector center = new Vector(2);

        Vector gravityDir = new Vector(2);


        // TODO Auto-generated method stub
        for (Body body : bodies) {
            gravityDir = center.minus(body.getPosition()).direction();
            body.setAcceleration(gravityDir.scale(g));
            gravityDir.equals(body.getAcceleration());

        }
    }

    public String toString() {
        return ("Falling to center gravity");
    }

}
