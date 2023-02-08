package infra.utils.request;

import java.util.HashMap;
import java.util.Map;

public abstract class MyRequestMap {

    Map<String, String> map = new HashMap<>();

    public void put(String key, String value) {
        this.map.put(key, value);
    }

    public String get(String key) {
        return this.map.get(key);
    }

    public boolean contains(String key) {
        return this.map.containsKey(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

}
