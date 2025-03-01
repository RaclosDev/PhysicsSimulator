package simulator.factories;

import org.json.JSONObject;
import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws> {

    public NewtonUniversalGravitationBuilder() {
        super("nlug", "Newton Lay Of Universal Gravitation");
    }

    @Override
    protected GravityLaws createTheInstance(JSONObject jsonObject) {
        return new NewtonUniversalGravitation();
    }
}
