package utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class QueryStringParser {
    public Map parseQueryString(String string) {
        return Arrays.stream(string.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(split -> split[0], split -> split[1]));
    }
}
