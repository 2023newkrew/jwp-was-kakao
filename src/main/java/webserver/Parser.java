package webserver;

import common.HttpHeader;
import common.HttpMethod;
import common.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.IllegalMethodException;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final String DELIM_QUERY_STRING = "?";
    private static final String DELIM_QUERY_PARAMETERS = "&";
    private static final String DELIM_KEY_VALUE = "=";
    private static final String DELIM_REQUEST_START_LINE = " ";
    private static final String DELIM_COOKIE = ";";

    public static HttpRequest parseRequest(BufferedReader reader) throws IOException {
        List<String> lines = readRequestLines(reader);
        String startLine = lines.get(0);
        String uri = getURI(startLine);
        HttpMethod method = getMethod(startLine);

        Map<String, String> headers = getHeaders(lines.subList(1, lines.size()));
        Map<String, String> parameters = Map.of();
        if (method.equals(HttpMethod.GET)) {
            parameters = getUriParameters(uri);
        }
        else if (method.equals(HttpMethod.POST)) {
            String contentLength = Optional.ofNullable(headers.get(HttpHeader.CONTENT_LENGTH.value())).orElse("0");
            parameters = getBodyParameters(reader, Integer.parseInt(contentLength));
        }
        Map<String, String> cookies = getCookies(headers.get(HttpHeader.COOKIE.value()));
        return new HttpRequest(uri.replaceAll("\\?.*", ""), method, headers, parameters, cookies);
    }

    private static Map<String, String> getHeaders(List<String> headerLines) {
        return headerLines.stream().collect(Collectors.toMap(
                (line) -> line.split(":")[0].trim(),
                (line) -> line.split(":")[1].trim()
        ));
    }

    private static List<String> readRequestLines(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while (!"".equals(line = reader.readLine())) {
            lines.add(line);
            logger.debug(line);
        }
        return lines;
    }

    private static HttpMethod getMethod(final String startLine) {
        try {
            return HttpMethod.valueOf(startLine.split(DELIM_REQUEST_START_LINE)[0]);
        } catch (IllegalArgumentException e) {
            throw new IllegalMethodException();
        }
    }

    public static String getURI(final String startLine) {
        return startLine.split(DELIM_REQUEST_START_LINE)[1];
    }

    public static Map<String, String> getUriParameters(final String uri) {
        if (!uri.contains(DELIM_QUERY_STRING)) {
            return new HashMap<>();
        }
        return getParametersFromLine(uri.split("\\?")[1]);
    }

    public static Map<String, String> getBodyParameters(BufferedReader reader, int contentLength) throws IOException {
        String body = IOUtils.readData(reader, contentLength);
        return getParametersFromLine(body);
    }

    private static Map<String, String> getParametersFromLine(final String line) {
        if (line.isEmpty()) return new HashMap<>();

        return Stream.of(line.split(DELIM_QUERY_PARAMETERS))
                .collect(Collectors.toMap(
                    (param)-> param.split(DELIM_KEY_VALUE)[0],
                    (param) -> param.split(DELIM_KEY_VALUE)[1]
                ));
    }

    public static Map<String, String> getCookies(String cookieHeader) {
        if (cookieHeader == null || cookieHeader.isBlank()) {
            return Map.of();
        }
        List<String> cookies = List.of(cookieHeader.split(DELIM_COOKIE));
        return cookies
            .stream()
            .collect(Collectors.toMap(
                    (cookie)-> cookie.split(DELIM_KEY_VALUE)[0],
                    (cookie) -> cookie.split(DELIM_KEY_VALUE)[1]
            ));
    }
}
