package utils;

import exception.BadRequestException;
import http.HttpMethod;
import http.HttpRequestHeader;
import http.Uri;
import http.request.RequestHeaders;
import http.request.RequestStartLine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestParsingUtils {
    private static final int START_LINE_SIZE = 3;
    private static final int KEY_VALUE_TUPLE_SIZE = 2;
    private static final String REGEX_COLON = ":";
    private static final String REGEX_SEMICOLON = ";";
    private static final String REGEX_AMPERSAND = "&";
    private static final String REGEX_EQUAL_SIGN = "=";
    public static RequestStartLine parseStartLine(String rawStartLine) {
        String[] splittedLine = rawStartLine.split(" ");
        if (splittedLine.length != START_LINE_SIZE) {
            throw new BadRequestException();
        }
        return new RequestStartLine(
                HttpMethod.valueOf(splittedLine[0]),
                new Uri(splittedLine[1]),
                splittedLine[2]
        );
    }

    public static RequestHeaders parseRequestHeader(List<String> rawHeader) {
        Map<HttpRequestHeader, String> parsedHeader = rawHeader.stream()
                .map(header -> header.split(REGEX_COLON, KEY_VALUE_TUPLE_SIZE))
                .filter(kv -> Objects.nonNull(HttpRequestHeader.of(kv[0].trim())))
                .collect(Collectors.toMap(
                        kv -> HttpRequestHeader.of(kv[0].trim()),
                        kv -> kv[1].trim()
                ));

        return new RequestHeaders(parsedHeader);
    }

    public static Map<String, String> parseQueryString(String queryString) {
        return parseKeyValueItemsByRegex(queryString, REGEX_AMPERSAND);
    }

    public static Map<String, String> parseCookie(String rawCookie) {
        return parseKeyValueItemsByRegex(rawCookie, REGEX_SEMICOLON);
    }

    private static Map<String, String> parseKeyValueItemsByRegex(String raw, String regex) {
        return Arrays.stream(raw.split(regex))
                .map(item -> item.split(REGEX_EQUAL_SIGN))
                .filter(item -> item.length == KEY_VALUE_TUPLE_SIZE)
                .collect(Collectors.toMap(
                        kv -> kv[0].trim(),
                        kv -> kv[1].trim()
                ));
    }
}
