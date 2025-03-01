package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Abstract builder class for creating instances of type T from JSON data.
 *
 * @param <T> The type of object to be built.
 */
public abstract class Builder<T> {

    private final String typeTag;
    private final String description;

    /**
     * Constructs a Builder with the specified type tag and description.
     *
     * @param typeTag     The type tag for the builder.
     * @param description The description of the builder.
     * @throws NullPointerException if typeTag or description is null.
     */
    public Builder(String typeTag, String description) {
        this.typeTag = Objects.requireNonNull(typeTag, "Type tag cannot be null.");
        this.description = Objects.requireNonNull(description, "Description cannot be null.");
    }

    /**
     * Converts a JSONArray to a double array.
     *
     * @param jsonArray The JSONArray to convert.
     * @return A double array containing the values from the JSONArray.
     * @throws NullPointerException if the JSONArray is null.
     */
    protected double[] jsonArrayToDoubleArray(JSONArray jsonArray) {
        Objects.requireNonNull(jsonArray, "JSONArray cannot be null.");
        double[] doubleArray = new double[jsonArray.length()];
        for (int i = 0; i < doubleArray.length; i++) {
            doubleArray[i] = jsonArray.getDouble(i);
        }
        return doubleArray;
    }

    /**
     * Creates an instance of type T from the provided JSON info.
     *
     * @param info The JSONObject containing the type and data.
     * @return An instance of type T, or null if the type does not match.
     * @throws NullPointerException if the info is null.
     * @throws IllegalArgumentException if the JSON data is invalid.
     */
    public T createInstance(JSONObject info) {
        Objects.requireNonNull(info, "JSON info cannot be null.");

        if (typeTag.equals(info.getString("type"))) {
            JSONObject data = info.getJSONObject("data");
            return createTheInstance(data);
        }
        return null;
    }

    /**
     * Returns the builder's information as a JSONObject.
     *
     * @return A JSONObject containing the type, data, and description.
     */
    public JSONObject getBuilderInfo() {
        JSONObject info = new JSONObject();
        info.put("type", typeTag);
        info.put("data", createData());
        info.put("desc", description);
        return info;
    }

    /**
     * Creates a JSONObject containing the default data for the builder.
     *
     * @return A JSONObject with the default data.
     */
    protected JSONObject createData() {
        return new JSONObject();
    }

    /**
     * Creates an instance of type T from the provided JSON data.
     *
     * @param data The JSONObject containing the data for the instance.
     * @return An instance of type T.
     * @throws NullPointerException if the data is null.
     * @throws IllegalArgumentException if the JSON data is invalid.
     */
    protected abstract T createTheInstance(JSONObject data);
}