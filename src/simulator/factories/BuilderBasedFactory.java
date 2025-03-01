package simulator.factories;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BuilderBasedFactory<T> implements Factory<T> {

    List<Builder<T>> builders;            //que es una lista de constructores.
    List<JSONObject> factoryElements;    //una lista de objetos JSON construídos por defecto.

    public BuilderBasedFactory(List<Builder<T>> builderList) {
        this.builders = builderList;
        this.factoryElements = new ArrayList<>();
        for (Builder builder : builderList) {
            this.factoryElements.add(builder.getBuilderInfo());
        }
    }

    @Override
    public T createInstance(JSONObject info) {

        T instance = null;
        for (Builder<T> builder : builders) {
            if (builder.createInstance(info) != null) {
                instance = builder.createInstance(info);
            }
        }

        return instance;
    }

    @Override
    public List<JSONObject> getInfo() {

        // TODO Auto-generated method stub
        return factoryElements;
    }

}
