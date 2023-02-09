package model.http;

import exception.EmptyHeaderException;

import java.util.Arrays;
import java.util.stream.Collectors;

import static utils.HttpUtils.KEY_VALUE_DELIMITER;
import static utils.HttpUtils.QUERY_DELIMITER;

public class CustomHttpRequestLine {

    private static final String LINE_DELIMITER = " ";
    private static final String URL_DELIMITER = "\\?";

    private final CustomHttpMethod httpMethod;
    private final String url;
    private final CustomHttpRequestQuery query;
    private final String protocol;

    public CustomHttpRequestLine(CustomHttpMethod httpMethod, String url, CustomHttpRequestQuery query, String protocol) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.query = query;
        this.protocol = protocol;
    }

    public CustomHttpRequestLine(String line) {
        validateLine(line);
        String[] tokens = line.split(LINE_DELIMITER);
        httpMethod = CustomHttpMethod.from(tokens[0]);
        String[] urls = tokens[1].split(URL_DELIMITER);
        url = urls[0];
        query = hasQuery(urls) ? getQuery(urls[1]) : null;
        protocol = tokens[2];
    }

    private static void validateLine(String line) {
        if (line == null || line.isEmpty()) {
            throw new EmptyHeaderException("잘못된 요청 형식입니다.");
        }
    }

    private static boolean hasQuery(String[] urls) {
        return urls.length > 1;
    }

    private static CustomHttpRequestQuery getQuery(String queryString) {
        return new CustomHttpRequestQuery(Arrays.stream(queryString.split(QUERY_DELIMITER))
                .map(field -> field.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(f -> f[0], f -> f[1])));
    }

    public CustomHttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public CustomHttpRequestQuery getQuery() {
        return query;
    }

    public String getProtocol() {
        return protocol;
    }
}
