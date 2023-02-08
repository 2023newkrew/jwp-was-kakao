package utils.parser;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class QueryStringParser {
    private final String QUERY_STRING_EACH_PARAMETER_DELIMITER = "&";
    private final String QUERY_STRING_KEY_VALUE_DELIMITER = "=";

    public Map<String, String> parseQueryString(String string) {
        return Arrays.stream(string.split(QUERY_STRING_EACH_PARAMETER_DELIMITER))
                .map(queryString -> queryString.split(QUERY_STRING_KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(getKey(), getValue()));
    }

    private Function<String[], String> getKey() {
        return queryStringToken -> queryStringToken[0];
    }

    private Function<String[], String> getValue() {
        return queryStringToken -> queryStringToken[1];
    }
}
