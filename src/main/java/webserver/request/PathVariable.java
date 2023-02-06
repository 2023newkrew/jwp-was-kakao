package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class PathVariable {

    private final Map<String, String> variableMap;

    public PathVariable(String pathVariable) {this.variableMap = parsePathVariable(pathVariable);}

    private Map<String, String> parsePathVariable(String pathVariable) {
        Map<String, String> variableMap = new HashMap<String, String>();
        for (String variable : pathVariable.split("&")) {
            String[] splitVariable = variable.split("=");
            variableMap.put(splitVariable[0], splitVariable[1]);
        }
        return variableMap;
    }

    public String get(String key) {
        return variableMap.get(key);
    }
}
