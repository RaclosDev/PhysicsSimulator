package simulator.factories;

import org.json.JSONObject;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body> {

    public MassLosingBodyBuilder() {
        super("mlb", "Mass losing body");
    }

    @Override
    protected Body createTheInstance(JSONObject data) {
        double[] p = jsonArrayToDoubleArray(data.getJSONArray("pos"));
        double[] v = jsonArrayToDoubleArray(data.getJSONArray("vel"));
        String id = data.getString("id");
        double m = data.getDouble("mass");
        double freq = data.getDouble("freq");
        double factor = data.getDouble("factor");

        return new MassLossingBody(id, new Vector(v), new Vector(2), new Vector(p), m, freq, factor);
    }

    @Override
    protected JSONObject createData() {
        JSONObject data = new JSONObject();
        data.put("id", "the identifier");
        data.put("vel", "the velocity vector");
        data.put("pos", "the position vector");
        data.put("mass", "the mass value");
        data.put("freq", "time to loss mass");
        data.put("factor", "loss mass factor");
        return data;
    }
}