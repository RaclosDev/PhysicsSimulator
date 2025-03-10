package simulator.factories;

import org.json.JSONObject;
import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder extends Builder<GravityLaws> {

    public FallingToCenterGravityBuilder() {
        super("ftcg", "Falling To Center Of Gravity");
    }

    @Override
    protected GravityLaws createTheInstance(JSONObject jsonObject) {
        return new FallingToCenterGravity();
    }
}
