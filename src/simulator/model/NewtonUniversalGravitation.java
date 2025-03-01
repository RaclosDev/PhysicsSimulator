package simulator.model;

import simulator.misc.Vector;

import java.util.List;

public class NewtonUniversalGravitation implements GravityLaws {
    static private final double G = 6.67E-11; // the gravitational constant

    public NewtonUniversalGravitation() {
    }

    private Vector force(Body a, Body b) { //Que calcula la fuerza que genera el cuerpo b sobre el cuerpo a, es decir fab.
        Vector v = new Vector(2);
        double gm1m2 = G * a.getMass() * b.getMass();
        v = (b.getPosition().minus(a.getPosition()));
        gm1m2 = gm1m2 / v.dot(v);
        v = b.posicion.minus(a.posicion).direction();
        v = v.scale(gm1m2);


        /*Vector v = null;
        double prod = G * a.getMass() * b.getMass();
        prod = prod / (b.getPosition().minus(a.getPosition())).dot(b.getPosition().minus(a.getPosition()));
        v = (b.getPosition().minus(a.getPosition())).direction();
        v = v.scale(prod);*/
        return v;
    }


    @Override
    public void apply(List<Body> bodies) {
        // TODO Auto-generated method stub
        Vector v = new Vector(2);
        v.scale(0);
        for (Body body : bodies) {
            v = new Vector(2);
            if (body.getMass() > 0) {
                for (Body body2 : bodies) {
                    if (!body.equals(body2)) {
                        //body.setAcceleration(force(body, body2).scale(1 / body.getMass()));
                        v = v.plus(force(body, body2));
                    }
                }
                body.setAcceleration(v.scale(1 / body.getMass()));
            } else {
                body.setAcceleration(v);
            }
        }
    }

    public String toString() {
        return ("Newton’s law of universal gravitation");
    }
}
