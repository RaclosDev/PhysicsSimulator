package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Builder<T> {

    String typeTag;
    String descripcion;

    public Builder(String typeTag, String descripcion) {
        this.typeTag = typeTag;
        this.descripcion = descripcion;
    }

    protected double[] jsonArrayTodoubleArray(JSONArray jsonArray) {
        double[] da = new double[jsonArray.length()];
        for (int i = 0; i < da.length; i++)
            da[i] = jsonArray.getDouble(i);
        return da;
    }

    public T createInstance(JSONObject info) {

        T b = null;
        if (typeTag != null && typeTag.equals(info.getString("type")))
            b = createTheInstance(info.getJSONObject("data"));
        return b;
    }

    public JSONObject getBuilderInfo() {
        JSONObject info = new JSONObject();
        info.put("type", typeTag);
        info.put("data", createData());
        info.put("desc", descripcion);
        return info;
    }

    protected JSONObject createData() {
        return new JSONObject();
    }

    protected abstract T createTheInstance(JSONObject jsonObject);


}