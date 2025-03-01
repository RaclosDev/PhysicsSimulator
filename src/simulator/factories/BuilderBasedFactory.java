package simulator.factories;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuilderBasedFactory<T> implements Factory<T> {

    private final List<Builder<T>> builders;
    private final List<JSONObject> factoryElements;

    public BuilderBasedFactory(List<Builder<T>> builderList) {
        this.builders = Objects.requireNonNull(builderList);
        this.factoryElements = new ArrayList<>();
        for (Builder<T> builder : builderList) {
            factoryElements.add(builder.getBuilderInfo());
        }
    }

    @Override
    public T createInstance(JSONObject info) {
        Objects.requireNonNull(info);
        for (Builder<T> builder : builders) {
            T instance = builder.createInstance(info);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public List<JSONObject> getInfo() {
        return factoryElements;
    }
}