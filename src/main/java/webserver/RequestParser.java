package webserver;

import java.util.*;
import java.util.stream.Collectors;

public class RequestParser {
    public static RequestHeader parseHeader(List<String> request) {
        RequestHeader.RequestHeaderBuilder requestHeaderBuilder = RequestHeader.builder();
        Iterator<String> iterator = request.iterator();

        extractStartLine(requestHeaderBuilder, iterator);
        extractHeader(requestHeaderBuilder, iterator);

        return requestHeaderBuilder.build();
    }

    private static void extractStartLine(RequestHeader.RequestHeaderBuilder requestHeaderBuilder, Iterator<String> iterator) {
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
        return Arrays.stream(params.split("&"))
                .map(v -> v.split("="))
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
    }

    private static void extractHeader(RequestHeader.RequestHeaderBuilder requestHeaderBuilder, Iterator<String> iterator) {
        Map<String, String> headers = new HashMap<>();
        while (iterator.hasNext()) {
            String[] splitedHeader = iterator.next().split(": ");
            headers.put(splitedHeader[0], splitedHeader[1]);
        }

        requestHeaderBuilder.headers(headers);
    }
}
