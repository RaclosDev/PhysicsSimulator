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

public class Controller {

    PhysicsSimulator sim;
    Factory<Body> bodiesFactory;
    Factory<GravityLaws> lawsFactory;

    public Controller(PhysicsSimulator sim, Factory<Body> bodiesFactory, Factory<GravityLaws> lawsFactory) {
        this.sim = sim;
        this.bodiesFactory = bodiesFactory;
        this.lawsFactory = lawsFactory;
    }

    public void loadBodies(InputStream in) {
        JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
        JSONArray bodies = jsonInupt.getJSONArray("bodies");
        for (int i = 0; i < bodies.length(); i++)
            sim.addBody(bodiesFactory.createInstance(bodies.getJSONObject(i)));
    }

    public void run(int steps, OutputStream out){
        PrintStream p = (out == null) ? null : new PrintStream(out);
        p.print("{\r\n" + 
        		"\"states\": [ \r\n");
            p.print(sim.toString());
        p.print("," +  "\r\n");

        for (int i = 0; i<steps;i++){
            sim.advance();
            p.print(sim.toString());
            if(i<steps-1) {
            p.print("," +  "\r\n");
            }
            else {
            	p.print("\r\n");
            }
        }
        p.print("]\r\n" + 
        		"        	}");
        
    }

    public void reset(){
        sim.reset();
    }

    public void setDeltaTime(double dt){
        sim.setDeltaTime(dt);
    }

    public void addObserver(SimulatorObserver o){
        sim.addObserver(o);
    }

    public void run(int steps){
        for (int i = 0; i<steps;i++){
            sim.advance();
        }
    }

    public Factory<GravityLaws> getGravityLawsFactory(){
        return lawsFactory;
    }

    public void setGravityLaws(JSONObject info){
        GravityLaws newLaw = null;
        for (JSONObject fe : lawsFactory.getInfo()) {
            if (info.equals(fe)) {
                newLaw = lawsFactory.createInstance(fe);
                break;
            }
        }
        sim.setGravityLaws(newLaw);
    }


}
