package simulator.factories;

import org.json.JSONObject;
import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {


    public BasicBodyBuilder() {
        super("basic", "Default Body");
    }

    @Override
    protected Body createTheInstance(JSONObject data) {
        // TODO Auto-generated method stub
        double[] p = jsonArrayTodoubleArray(data.getJSONArray("pos"));
        double[] v = jsonArrayTodoubleArray(data.getJSONArray("vel"));
        String id = data.getString("id");
        double m = data.getDouble("mass");

        return new Body(id, new Vector(v), new Vector(2), new Vector(p), m);
    }

    protected JSONObject createData() {
        JSONObject data = new JSONObject();

        data.put("id", "the identifier");
        data.put("vel", "the velocity vector");
        data.put("pos", "the position vector");
        data.put("mass", "the mass value");
        return null;
    }
}
