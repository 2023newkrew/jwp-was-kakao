package webserver.model.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import webserver.utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Request {
    private final Method method;
    private final String path;
    private final Map<String, String> queryString;
    private final String protocol;
    private final Map<String, String> requestHeader;
    private final Cookie cookie;
    private final String requestBody;

    public static Request parse(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();

        Method method = parseMethod(firstLine);
        String path = parsePath(firstLine);
        Map<String, String> queryString = parseQueryString(firstLine);
        String protocol = parseProtocol(firstLine);

        Map<String, String> requestHeader = parseHeader(reader);
        Cookie cookie = parseCookie(requestHeader);
        String requestBody = parseBody(reader, requestHeader);

        return Request.builder()
                .method(method)
                .path(path)
                .queryString(queryString)
                .protocol(protocol)
                .requestHeader(requestHeader)
                .cookie(cookie)
                .requestBody(requestBody)
                .build();
    }

    private static Method parseMethod(String firstLine) {
        return Method.of(firstLine.split(RequestConstant.WHITE_SPACE_REGEX)[RequestConstant.METHOD_INDEX]);
    }

    private static String parsePath(String firstLine) {
        return firstLine.split(RequestConstant.WHITE_SPACE_REGEX)[RequestConstant.URL_INDEX].split(RequestConstant.QUERY_STRING_IDENTIFIER)[RequestConstant.PATH_INDEX];
    }

    private static Map<String, String> parseQueryString(String firstLine) {
        String[] splitUrl = firstLine.split(RequestConstant.WHITE_SPACE_REGEX)[RequestConstant.URL_INDEX].split(RequestConstant.QUERY_STRING_IDENTIFIER);
        if (splitUrl.length == 1) {
            return Map.of();
        }
        return parseQueryStringFormat(splitUrl[RequestConstant.QUERY_STRING_INDEX]);
    }

    private static String parseProtocol(String firstLine) {
        return firstLine.split(RequestConstant.WHITE_SPACE_REGEX)[RequestConstant.PROTOCOL_INDEX];
    }

    private static Map<String, String> parseHeader(BufferedReader reader) throws IOException {
        Map<String, String> requestHeader = new HashMap<>();
        String header;
        while (!Objects.equals(header = reader.readLine(), "")) {
            String[] headerInformation = header.split(RequestConstant.HEADER_KEY_SEPARATOR);
            requestHeader.put(headerInformation[RequestConstant.KEY_INDEX].trim(), headerInformation[RequestConstant.VALUE_INDEX].trim());
        }
        return Collections.unmodifiableMap(requestHeader);
    }

    private static Cookie parseCookie(Map<String, String> requestHeader) {
        if (requestHeader.containsKey(RequestConstant.COOKIE)) {
            return Cookie.of(requestHeader.get(RequestConstant.COOKIE));
        }
        return Cookie.empty();
    }

    private static String parseBody(BufferedReader reader, Map<String, String> requestHeader) throws IOException {
        if (requestHeader.containsKey(RequestConstant.CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(requestHeader.get(RequestConstant.CONTENT_LENGTH));
            return IOUtils.readData(reader, contentLength);
        }
        return "";
    }

    private static Map<String, String> parseQueryStringFormat(String input) {
        return Arrays.stream(input.split(RequestConstant.QUERY_STRING_CONNECTOR))
                .map(s -> s.split(RequestConstant.QUERY_STRING_SEPARATOR))
                .collect(Collectors.toMap(keyValuePair -> keyValuePair[RequestConstant.KEY_INDEX], keyValuePair -> keyValuePair[RequestConstant.VALUE_INDEX], (a, b) -> b));
    }

    public Map<String, String> getRequestBodyAsQueryString() {
        return parseQueryStringFormat(requestBody);
    }

    public FileType getRequestFileType() {
        String[] split = path.split(RequestConstant.PERIOD_REGEX);
        String fileExtension = split[split.length - 1];
        return FileType.findType(fileExtension);
    }

    public Optional<String> getSession() {
        return cookie.getAttribute(RequestConstant.SESSION_KEY);
    }
}
