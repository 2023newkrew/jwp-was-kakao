package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final String DELIM_QUERY_STRING = "?";
    private static final String DELIM_QUERY_PARAMETERS = "&";
    private static final String DELIM_QUERY_PARAMETER = "=";
    private static final String DELIM_REQUEST_START_LINE = " ";

    private static final String HTML_FILE_PATH = "./templates";
    private static final String CSS_FILE_PATH = "./static";

    public static Map<String, String> getUriParameters(String uri) {
        if (!uri.contains(DELIM_QUERY_STRING)) {
            return new HashMap<>();
        }
        return getParameters(uri.split("\\?")[1]);
    }

    private static Map<String, String> getParameters(final String queryString) {
        List<String> queryParams = List.of(queryString.split(DELIM_QUERY_PARAMETERS));
        return queryParams
                .stream()
                .collect(Collectors.toMap(
                    (param)-> param.split(DELIM_QUERY_PARAMETER)[0],
                    (param) -> param.split(DELIM_QUERY_PARAMETER)[1]
                ));
    }

    public static byte[] getFileContent(final String uri) {
        try {
            if (uri.endsWith(".html")) {
                return FileIoUtils.loadFileFromClasspath(HTML_FILE_PATH + uri.replaceFirst("^\\.+", ""));
            } else if (uri.endsWith(".css")) {
                return FileIoUtils.loadFileFromClasspath(CSS_FILE_PATH + uri.replaceFirst("^\\.+", ""));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "".getBytes();
    }

    public static HttpRequest parseRequestMessage(final BufferedReader reader) {
        String startLine = readRequestStartLine(reader);
        String uri = getURI(startLine);
        HttpMethod method = getMethod(startLine);

        Map<String, String> parameters = Map.of();
        if (method.equals(HttpMethod.GET)) {
            parameters = getUriParameters(uri);
        }
        if (method.equals(HttpMethod.POST)) {
            List<String> body = readRequestLines(reader);
            parameters = getBody(body);
        }
        return new HttpRequest(uri, method, parameters);
    }

    private static String readRequestStartLine(final BufferedReader reader) {
        return readRequestLines(reader).get(0);
    }

    private static List<String> readRequestLines(final BufferedReader reader) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while (!"".equals(line = reader.readLine())) {
                if (Objects.isNull(line)) break;
                lines.add(line);
                logger.debug(line);
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        return lines;
    }

    private static HttpMethod getMethod(final String startLine) {
        return HttpMethod.valueOf(startLine.split(DELIM_REQUEST_START_LINE)[0]);
    }

    public static String getURI(final String startLine) {
        return startLine.split(DELIM_REQUEST_START_LINE)[1];
    }

    public static Map<String, String> getBody(final List<String> body) {
        // TODO : JSON 형태 body 예외처리
        return getParameters(body.get(0));
    }
}
