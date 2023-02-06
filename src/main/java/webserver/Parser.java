package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

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
        List<String> startLineAndHeaders = readRequestLines(reader);
        Map<String, String> parameters = new HashMap<>();

        HttpMethod method = getMethod(startLineAndHeaders);
        String uri = getURI(startLineAndHeaders);
        String[] pathAndParameters = uri.split("\\?");
        String path = pathAndParameters[0];

        if (method.equals(HttpMethod.GET) && pathAndParameters.length == 2) {
            parameters = getParameters(pathAndParameters[1]);
        }
        else if (method.equals(HttpMethod.POST)) {
            List<String> body = readRequestLines(reader);
            parameters.putAll(getParameters(body.get(0)));
        }
        return new HttpRequest(path, method, parameters);
    }

    private static List<String> readRequestLines(final BufferedReader reader) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while (!"".equals(line = reader.readLine())) {
                if (Objects.isNull(line)) break;
                lines.add(line);
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        return lines;
    }

    private static HttpMethod getMethod(final List<String> requestLines) {
        return HttpMethod.valueOf(requestLines.get(0).split(" ")[0]);
    }

    private static String getURI(final List<String> requestLines) {
        return requestLines.get(0).split(" ")[1];
    }
}