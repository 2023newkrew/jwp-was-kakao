package webserver;

import java.util.*;

public class RequestParser {
    public static RequestHeader parseHeader(List<String> request) {
        RequestHeader.RequestHeaderBuilder requestHeaderBuilder = RequestHeader.builder();
        ListIterator<String> iterator = request.listIterator();

        extractStartLine(requestHeaderBuilder, iterator);
        extractHeader(requestHeaderBuilder, iterator);

        return requestHeaderBuilder.build();
    }

    private static void extractStartLine(RequestHeader.RequestHeaderBuilder requestHeaderBuilder, ListIterator<String> iterator) {
        String[] startLine = iterator.next().split(" ");

        String[] splitedUrl = startLine[1].split("\\?");
        String url = splitedUrl[0];
        Map<String, String> queryParams = new HashMap<>();
        if (splitedUrl.length > 1) {
            queryParams = extractQueryParams(splitedUrl[1]);
        }

        requestHeaderBuilder.httpMethod(HttpMethod.valueOf(startLine[0]))
                .url(url)
                .queryParams(queryParams)
                .httpVersion(startLine[2]);
    }

    private static Map<String, String> extractQueryParams(String params) {
        Map<String, String> queryParams = new HashMap<>();
        Arrays.stream(params.split("&"))
                .forEach(v -> {
                    String[] kv = v.split("=");
                    queryParams.put(kv[0], kv[1]);
                });

        return queryParams;
    }

    private static void extractHeader(RequestHeader.RequestHeaderBuilder requestHeaderBuilder, ListIterator<String> iterator) {
        Map<String, String> headers = new HashMap<>();
        while (iterator.hasNext()) {
            String[] splitedHeader = iterator.next().split(": ");
            headers.put(splitedHeader[0], splitedHeader[1]);
        }

        requestHeaderBuilder.headers(headers);
    }
}
