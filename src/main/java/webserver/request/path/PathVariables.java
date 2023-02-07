package webserver.request.path;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PathVariables {

    private static final String PARAM_DELIMITER = "&";

    private static final String VALUE_DELIMITER = "=";

    private final Map<String, String> variableMap;

    public PathVariables(String pathVariables) {this.variableMap = parsePathVariable(pathVariables);}

    private Map<String, String> parsePathVariable(String pathVariables) {
        return Arrays.stream(pathVariables.split(PARAM_DELIMITER))
                .map(pathVariable -> pathVariable.split(VALUE_DELIMITER))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
    }

    public String get(String key) {
        return variableMap.get(key);
    }
}
