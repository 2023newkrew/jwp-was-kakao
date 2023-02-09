package utils;

import http.HttpRequestHeader;
import http.HttpStartLine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParsingUtils {
    public static Map<HttpStartLine, String> parseStartLine(String rawStartLine) {
        String[] splittedLine = rawStartLine.split(" ");
        return new HashMap<>() {{
            put(HttpStartLine.METHOD, splittedLine[0]);
            put(HttpStartLine.URI, splittedLine[1]);
            put(HttpStartLine.VERSION, splittedLine[2]);
        }};
    }

    public static Map<HttpRequestHeader, String> parseRequestHeader(List<String> rawHeader) {
        return rawHeader.stream()
                .map(header -> header.split(":", 2))
                .collect(Collectors.toMap(
                        kv -> HttpRequestHeader.of(kv[0].trim()),
                        kv -> kv[1].trim()
                ));
    }

    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> newParams = new HashMap<>();
        Arrays.stream(queryString.split("&"))
                .forEach(item -> {
                    String[] tuple = item.split("=");
                    if (tuple.length == 2) {
                        newParams.put(tuple[0], tuple[1]);
                    }
                });

        return newParams;
    }

}
