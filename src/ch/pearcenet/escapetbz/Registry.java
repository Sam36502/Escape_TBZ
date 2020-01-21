package ch.pearcenet.escapetbz;

import java.util.HashMap;

public class Registry {

    private HashMap<String, String> registry;

    public Registry() {
        registry = new HashMap<>();
    }

    public String getString(String key) {
        return registry.get(key);
    }

    public int getInt(String key) {
        int var;
        try {
            var = Integer.parseInt(registry.get(key));
        } catch (NumberFormatException e) {
            return -1;
        }

        return var;
    }

    public void createVar(String name) {
        registry.put(name, "");
    }

    public void setVar(String name, String val) {
        if (registry.containsKey(name)) {
            registry.replace(name, val);
        }
    }

    public void setVar(String name, int val) {
        setVar(name, ""+val);
    }

}
