package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsingUtils {
    public static Map<String, String> parseStartLine(String rawStartLine) {
        String[] splittedLine = rawStartLine.split(" ");
        return new HashMap<>() {{
            put("method", splittedLine[0]);
            put("uri", splittedLine[1]);
            put("version", splittedLine[2]);
        }};
    }

    public static Map<String, String> parseHeader(List<String> rawHeader) {
        Map<String, String> headers = new HashMap<>();

        rawHeader.forEach(item -> {
            String[] kv = item.split(":", 2);
            headers.put(kv[0].trim(), kv[1].trim());
        });
        return headers;
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
