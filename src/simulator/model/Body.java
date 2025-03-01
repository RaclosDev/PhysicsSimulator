package simulator.model;

import simulator.misc.Vector;

public class Body {

    protected String id;
    protected Vector velocidad;
    protected Vector aceleracion;
    protected Vector posicion;
    protected double masa;


    public Body(String id, Vector velocidad, Vector aceleracion, Vector posicion, double masa) {
        super();
        this.id = id;
        this.velocidad = velocidad;
        this.aceleracion = aceleracion;
        this.posicion = posicion;
        this.masa = masa;
    }

    public String getId() { //devuelve el identificador del cuerpo.
        return id;
    }

    public Vector getVelocity() { //devuelve una copia del vector de velocidad.
        return new Vector(velocidad);
    }

    public Vector getAcceleration() {//devuelve una copia del vector de aceleraci�n.
        return new Vector(aceleracion);
    }

    public Vector getPosition() { //devuelve una copia del vector de posici�n.
        return new Vector(posicion);
    }

    public double getMass() { // devuelve la masa del cuerpo.
        return masa;
    }

    void setVelocity(Vector v) { //hace una copia de v y se la asigna al vector de velocidad.
        velocidad = new Vector(v);
    }

    void setMass(double m) { //hace una copia de v y se la asigna al vector de velocidad.
        this.masa = m;
    }

    void setAcceleration(Vector a) { //hace una copia de a y se la asigna al vector de aceleracion.
        aceleracion = new Vector(a);
    }

    void setPosition(Vector p) { // hace una copia de p y se la asigna al vector de posici�n.
        posicion = new Vector(p);
    }

    void move(double t) {
    // update position
       posicion = posicion.plus(velocidad.scale(t)).plus((aceleracion.scale(Math.pow(t,2)*0.5))); //~p+~v x t
        //posicion = posicion.plus(velocidad.scale(t)); //~p+~v x t
        //posicion = posicion.plus((aceleracion.scale(1/2)).scale(Math.pow(t,2))); //+1/2 x ~a· t^2
    // update velocity vector
        velocidad = velocidad.plus(aceleracion.scale(t));
    }

    public String toString() {
        String infoBody = null;
        infoBody = " {  \"id\": \"" + getId() +
                "\", \"mass\": " + getMass() +
                ", \"pos\": " + getPosition() +
                ", \"vel\": " + getVelocity() +
                ", \"acc\": " + getAcceleration() +
                " } ";

        return infoBody;
    }


}
