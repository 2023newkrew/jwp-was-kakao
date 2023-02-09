package webserver.request;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryStringParser {

    public static Map<String, String> parseQueryString(String string, String separator) {
        return Arrays.stream(string.split(separator))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(split -> split[0], split -> split[1]));
    }
}
