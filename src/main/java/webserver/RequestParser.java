package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;
import webserver.http.HttpHeaders;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class RequestParser {
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
            } else {
                return FileIoUtils.loadFileFromClasspath("./static" + uri.replaceFirst("^\\.+", ""));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "".getBytes();
    }

    public static HttpRequest parseRequestMessage(final BufferedReader reader) {
        String startLine = readStartLine(reader);
        List<String> Headers = readHeaders(reader);

        String uri = getURI(startLine);
        HttpMethod method = getMethod(startLine);
        HttpHeaders headers = getHttpHeaders(Headers);
        String[] pathAndParameters = uri.split("\\?");
        String path = pathAndParameters[0];

        Map<String, String> parameters = new HashMap<>();

        if (method.equals(HttpMethod.GET) && pathAndParameters.length == 2) {
            parameters = getParameters(pathAndParameters[1]);
        }
        else if (method.equals(HttpMethod.POST)) {
            try {
                String data = IOUtils.readData(reader, Integer.parseInt(headers.getHeaders().get("Content-Length")));
                parameters.putAll(getParameters(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new HttpRequest(path, method, headers, parameters);
    }

    private static String readStartLine(final BufferedReader reader) {
        String startLine = null;
        try {
            startLine = reader.readLine();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return startLine;
    }

    private static HttpHeaders getHttpHeaders(final List<String> headerLines) {
        Map<String, String> headers = new HashMap<>();
        headerLines.forEach(line -> {
            System.out.println(line);
            String[] headerKeyAndValue = line.split(": ");
            headers.put(headerKeyAndValue[0], headerKeyAndValue[1]);
        });
        return new HttpHeaders(headers);
    }

    private static List<String> readHeaders(final BufferedReader reader) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while (Objects.nonNull(line = reader.readLine())) {
                System.out.println(line);
                if (line.isEmpty()) break;
                lines.add(line);
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        return lines;
    }

    private static HttpMethod getMethod(final String startLine) {
        return HttpMethod.valueOf(startLine.split(" ")[0]);
    }

    private static String getURI(final String startLine) {
        return startLine.split(" ")[1];
    }
}