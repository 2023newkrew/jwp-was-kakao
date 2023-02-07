package app;

import java.util.HashMap;
import java.util.Map;

public class RequestParameters {
    Map<String, String> parameters;

    public RequestParameters(String query) {
        this.parameters = new HashMap<>();
        this.put(query);
    }

    public String get(String key) {
        return parameters.get(key);
    }

    private void put(String query) {
        for (String elem : query.split("&")) {
            String[] param = elem.split("=");
            if (param.length != 2) {
                return;
            }
            parameters.put(param[0], param[1]);
        }
    }
}
