package utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class QueryStringParser {
    public Map<String, String> parseQueryString(String string) {
        String[] params = string.split("&");
        return Arrays.stream(params)
                .map(param -> param.split("="))
                .collect(Collectors.toMap(split -> split[0], split -> split[1]));
    }
}
