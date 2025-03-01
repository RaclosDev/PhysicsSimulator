package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector;
import simulator.model.Body;

import java.util.Objects;

/**
 * Builder for creating basic bodies.
 */
public class BasicBodyBuilder extends Builder<Body> {

    /**
     * Constructs a BasicBodyBuilder with the default type and description.
     */
    public BasicBodyBuilder() {
        super("basic", "Default Body");
    }

    /**
     * Creates a Body instance from the provided JSON data.
     *
     * @param data The JSON object containing the body's data.
     * @return A new Body instance.
     * @throws IllegalArgumentException if the JSON data is invalid.
     * @throws NullPointerException     if the JSON data is null.
     */
    @Override
    protected Body createTheInstance(JSONObject data) {
        Objects.requireNonNull(data, "JSON data cannot be null.");

        try {
            // Extract position, velocity, mass, and ID from the JSON data
            Vector position = jsonArrayToVector(data.getJSONArray("pos"));
            Vector velocity = jsonArrayToVector(data.getJSONArray("vel"));
            String id = data.getString("id");
            double mass = data.getDouble("mass");

            // Validate mass to ensure it is positive
            if (mass <= 0) {
                throw new IllegalArgumentException("Mass must be a positive value.");
            }

            // Create and return a new Body instance
            return new Body(id, velocity, new Vector(2), position, mass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON data for body creation.", e);
        }
    }

    /**
     * Converts a JSONArray to a Vector.
     *
     * @param jsonArray The JSONArray containing the vector components.
     * @return A new Vector instance.
     * @throws IllegalArgumentException if the JSONArray is invalid.
     */
    private Vector jsonArrayToVector(JSONArray jsonArray) {
        double[] components = new double[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            components[i] = jsonArray.getDouble(i);
        }
        return new Vector(components);
    }

    /**
     * Creates a JSONObject containing the template data for a basic body.
     *
     * @return A JSONObject with the template data.
     */
    @Override
    protected JSONObject createData() {
        JSONObject data = new JSONObject();
        data.put("id", "the identifier");
        data.put("vel", "the velocity vector");
        data.put("pos", "the position vector");
        data.put("mass", "the mass value");
        return data;
    }
}