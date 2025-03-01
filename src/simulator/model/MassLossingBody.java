package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body {

    private double lossFactor;
    private double lossFrequency;
    private double tiempoTotal;

    public MassLossingBody(String id, Vector velocidad, Vector aceleracion, Vector posicion, double masa, double lossFrequency,  double lossFactortor) {
        super(id, velocidad, aceleracion, posicion, masa);
        // TODO Auto-generated constructor stub
        this.lossFactor = lossFactortor;
        this.lossFrequency = lossFrequency;
        this.tiempoTotal = 0.0;
    }

    void move(double t) {
        super.move(t);
        tiempoTotal += t;
        if (tiempoTotal >= lossFrequency) {    //comprueba si han pasado lossFrequency	segundos
            setMass(getMass() * (1 - lossFactor));
            tiempoTotal = 0.0;
        }
    }

}
