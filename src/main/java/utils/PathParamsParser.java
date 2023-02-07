package utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class PathParamsParser {
    private final String path;
    private final Map<String, String> params;

    public PathParamsParser(String url) {
        String[] result = url.split("\\?",2);
        path = getPath(result);
        params = getParams(result);
    }

    private Map<String, String> getParams(String[] result) {
        if (result.length > 1) {
            return Arrays.stream(result[1].split("&"))
                    .map(it -> it.split("=", 2))
                    .filter(it -> it.length == 2)
                    .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));
        }
        return Map.of();
    }

    private String getPath(String[] result) {
        if (result.length > 0) {
            return result[0];
        }
        return "";
    }
}
