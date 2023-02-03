package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static Map<String, String> getUriParameters(String uri) {
        if (!uri.contains("?")) {
            return new HashMap<>();
        }
        return getParameters(uri.split("\\?")[1]);
    }

    private static Map<String, String> getParameters(final String queryString) {
        List<String> queryParams = List.of(queryString.split("&"));
        Map<String, String> parameters = new HashMap<>();
        queryParams.forEach((queryParam) -> parameters.put(queryParam.split("=")[0], queryParam.split("=")[1]));
        return parameters;
    }

    public static byte[] getFileContent(final String uri) {
        try {
            if (uri.endsWith(".html")) {
                return FileIoUtils.loadFileFromClasspath("./templates" + uri.replaceFirst("^\\.+", ""));
            } else if (uri.endsWith(".css")) {
                return FileIoUtils.loadFileFromClasspath("./static" + uri.replaceFirst("^\\.+", ""));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "".getBytes();
    }

    public static HttpRequest parseRequestMessage(final BufferedReader reader) {
        List<String> startLineAndHeader = readRequestLines(reader);
        List<String> body = readRequestLines(reader);

        String uri = getURI(startLineAndHeader);
        HttpMethod method = getMethod(startLineAndHeader);
        Map<String, String> parameters = new HashMap<>();
        if (method.equals(HttpMethod.GET)) {
            parameters = getUriParameters(uri);
        }
        if (method.equals(HttpMethod.POST)) {
            parameters.putAll(getBody(body));
        }
        return new HttpRequest(uri, method, parameters);
    }

    private static List<String> readRequestLines(final BufferedReader reader) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while (!"".equals(line = reader.readLine())) {
                if (Objects.isNull(line)) break;
                lines.add(line);
                System.out.println(line);
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        return lines;
    }

    private static HttpMethod getMethod(final List<String> requestMessage) {
        return HttpMethod.valueOf(requestMessage.get(0).split(" ")[0]);
    }

    public static String getURI(final List<String> requestMessage) {
        return requestMessage.get(0).split(" ")[1];
    }

    public static Map<String, String> getBody(final List<String> body) {
        return getParameters(body.get(0));
    }
}
