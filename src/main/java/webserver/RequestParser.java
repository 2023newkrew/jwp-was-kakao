package webserver;

import java.util.*;

public class RequestParser {
    public static Request parse(List<String> request) {
        Request.RequestBuilder requestDtoBuilder = Request.builder();
        ListIterator<String> iterator = request.listIterator();

        extractStartLine(requestDtoBuilder, iterator);
        extractHeader(requestDtoBuilder, iterator);
        if (iterator.hasNext()) {
            extractBody(requestDtoBuilder, iterator);
        }

        return requestDtoBuilder.build();
    }

    private static void extractStartLine(Request.RequestBuilder requestDtoBuilder, ListIterator<String> iterator) {
        String line = iterator.next();
        String[] startLine = line.split(" ");
        String[] splitedUrl = startLine[1].split("\\?");
        String url = splitedUrl[0];
        Map<String, String> queryParams = new HashMap<>();
        if (splitedUrl.length > 1) {
            queryParams = extractQueryParams(splitedUrl[1]);
        }

        requestDtoBuilder.httpMethod(HttpMethod.valueOf(startLine[0]))
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

    private static void extractHeader(Request.RequestBuilder requestDtoBuilder, ListIterator<String> iterator) {
        Map<String, String> headers = new HashMap<>();
        String line;
        line = iterator.next();
        while (!Objects.equals(line, "")) {
            String[] splitedHeader = line.split(": ");
            headers.put(splitedHeader[0], splitedHeader[1]);
            line = iterator.next();
        }

        requestDtoBuilder.headers(headers);
    }

    private static void extractBody(Request.RequestBuilder requestDtoBuilder, ListIterator<String> iterator) {
        String line;
        StringBuilder body = new StringBuilder();
        while (iterator.hasNext()) {
            line = iterator.next();
            body.append(line).append("\n");
        }
        body.deleteCharAt(body.length() - 1);

        requestDtoBuilder.body(body.toString());
    }
}
