package webserver;

import java.util.*;

public class RequestParser {
    public static Request parse(List<String> request) {
        Request.RequestBuilder requestDtoBuilder = Request.builder();

        ListIterator<String> iterator = request.listIterator();
        // 시작줄
        String line = iterator.next();
        String[] startLine = line.split(" ");
        requestDtoBuilder.httpMethod(HttpMethod.valueOf(startLine[0]))
                .url(startLine[1])
                .httpVersion(startLine[2]);

        // 헤더
        Map<String, String> headers = new HashMap<>();
        line = iterator.next();
        while (!Objects.equals(line, "")) {
            String[] splitedHeader = line.split(": ");
            headers.put(splitedHeader[0], splitedHeader[1]);
            line = iterator.next();
        }
        requestDtoBuilder.headers(headers);

        // 바디
        line = iterator.next();
        if (Objects.equals(line, "")) {
            return requestDtoBuilder.build();
        }
        StringBuilder body = new StringBuilder();
        iterator.previous();
        while (iterator.hasNext()) {
            line = iterator.next();
            body.append(line).append("\n");
        }
        body.deleteCharAt(body.length() - 1);
        requestDtoBuilder.body(body.toString());

        return requestDtoBuilder.build();
    }
}
