package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class MyParams {

    private final Map<String, String> params = new HashMap<>();

    public void put(String key, String value){
        params.put(key, value);
    }

    public String get(String key){
        return params.get(key);
    }
}
