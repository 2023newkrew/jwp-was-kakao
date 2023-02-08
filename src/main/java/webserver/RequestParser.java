package webserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import webserver.request.HttpMethod;
import webserver.request.StartLine;

public class RequestParser {

    public static StartLine extractStartLine(String line) {
        String[] startLine = line.split(" ");
        String[] splitedUrl = startLine[1].split("\\?");
        String url = splitedUrl[0];
        Map<String, String> queryParams = new HashMap<>();
        if (splitedUrl.length > 1) {
            queryParams = extractBodyOrQueryParam(splitedUrl[1]);
        }

        return StartLine.builder()
            .httpMethod(HttpMethod.valueOf(startLine[0]))
            .url(url)
            .queryParams(queryParams)
            .httpVersion(startLine[2]).build();
    }

    public static Map<String, String> extractHeader(List<String> headerList) {
        Map<String, String> headers = new HashMap<>();
        for (String header : headerList) {
            String[] splitedHeader = header.split(": ");
            headers.put(splitedHeader[0], splitedHeader[1]);
        }

        return headers;
    }

    public static Map<String, String> extractBodyOrQueryParam(String rawBody) {
        Map<String, String> body = new HashMap<>();
        if (rawBody != null) {
            Arrays.stream(rawBody.split("&"))
                .forEach(v -> {
                    String[] kv = v.split("=");
                    body.put(kv[0], kv[1]);
                });
        }
        return body;
    }
}
