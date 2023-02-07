package webserver.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.IOUtils;
import webserver.FileType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Request {

    // Request Information
    private final Method method;
    private final String path;
    private final Map<String, String> queryString;
    private final String protocol;
    private final Map<String, String> requestHeader;
    private final String requestBody;

    // String Constant
    private static final String WHITE_SPACE_REGEX = " ";
    private static final String PERIOD_REGEX = "\\.";
    private static final String HEADER_KEY_SEPARATOR = ":";
    private static final String QUERY_STRING_IDENTIFIER = "\\?";
    private static final String QUERY_STRING_CONNECTOR = "&";
    private static final String QUERY_STRING_SEPARATOR = "=";
    private static final String CONTENT_LENGTH = "Content-Length";

    // Method
    public static Request parse(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();

        Method method = parseMethod(firstLine);
        String path = parsePath(firstLine);
        Map<String, String> queryString = parseQueryString(firstLine);
        String protocol = parseProtocol(firstLine);

        Map<String, String> requestHeader = parseHeader(reader);
        String requestBody = parseBody(reader, requestHeader);

        return Request.builder()
                .method(method)
                .path(path)
                .queryString(queryString)
                .protocol(protocol)
                .requestHeader(requestHeader)
                .requestBody(requestBody)
                .build();
    }

    private static Method parseMethod(String firstLine) {
        return Method.of(firstLine.split(WHITE_SPACE_REGEX)[0]);
    }

    private static String parsePath(String firstLine) {
        return firstLine.split(WHITE_SPACE_REGEX)[1].split(QUERY_STRING_IDENTIFIER)[0];
    }

    private static Map<String, String> parseQueryString(String firstLine) {
        String[] splitUrl = firstLine.split(WHITE_SPACE_REGEX)[1].split(QUERY_STRING_IDENTIFIER);
        if (splitUrl.length == 1) {
            return new HashMap<>();
        }
        return parseQueryStringFormat(splitUrl[1]);
    }

    private static String parseProtocol(String firstLine) {
        return firstLine.split(WHITE_SPACE_REGEX)[2];
    }

    private static Map<String, String> parseHeader(BufferedReader reader) throws IOException {
        Map<String, String> requestHeader = new HashMap<>();
        String header;
        while (!Objects.equals(header = reader.readLine(), "")) {
            String[] headerInformation = header.split(HEADER_KEY_SEPARATOR);
            requestHeader.put(headerInformation[0].trim(), headerInformation[1].trim());
        }
        return requestHeader;
    }

    private static String parseBody(BufferedReader reader, Map<String, String> requestHeader) throws IOException {
        if (requestHeader.containsKey(CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(requestHeader.get(CONTENT_LENGTH));
            return IOUtils.readData(reader, contentLength);
        }
        return "";
    }

    private static Map<String, String> parseQueryStringFormat(String input) {
        return Arrays.stream(input.split(QUERY_STRING_CONNECTOR))
                .map(s -> s.split(QUERY_STRING_SEPARATOR))
                .collect(Collectors.toMap(keyValuePair -> keyValuePair[0], keyValuePair -> keyValuePair[1], (a, b) -> b));
    }

    public Map<String, String> getRequestBodyAsQueryString() {
        return parseQueryStringFormat(requestBody);
    }

    public FileType getRequestFileType() {
        String[] split = path.split(PERIOD_REGEX);
        String fileExtension = split[split.length - 1];
        return FileType.findType(fileExtension);
    }
}
