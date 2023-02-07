package webserver;

import common.HttpMethod;
import common.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.IllegalMethodException;
import support.UnsupportedContentTypeException;

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

    public static HttpRequest parseRequest(BufferedReader reader) {
        String startLine = readRequestStartLine(reader);
        String uri = getURI(startLine);
        HttpMethod method = getMethod(startLine);

        Map<String, String> parameters = Map.of();
        if (method.equals(HttpMethod.GET)) {
            parameters = getUriParameters(uri);
        }
        else if (method.equals(HttpMethod.POST)) {
            parameters = getBodyParameters(reader);
        }
        return new HttpRequest(uri, method, parameters);
    }

    private static String readRequestStartLine(BufferedReader reader) {
        return readRequestLines(reader).get(0);
    }

    private static List<String> readRequestLines(BufferedReader reader) {
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

    public static Map<String, String> getBodyParameters(BufferedReader reader) {
        List<String> lines = readRequestLines(reader);
        if (lines.isEmpty()) {
            return new HashMap<>();
        }
        else if (lines.size() > 1)  {
            throw new UnsupportedContentTypeException();
        }
        return getParametersFromLine(lines.get(0));
    }

    private static Map<String, String> getParametersFromLine(final String line) {
        List<String> params = List.of(line.split(DELIM_QUERY_PARAMETERS));
        return params
                .stream()
                .collect(Collectors.toMap(
                    (param)-> param.split(DELIM_QUERY_PARAMETER)[0],
                    (param) -> param.split(DELIM_QUERY_PARAMETER)[1]
                ));
    }
}
