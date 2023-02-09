package webserver.request;

import static utils.IOUtils.readData;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpMethod;
import webserver.FilenameExtension;

public class HttpRequest {
    private final HttpRequestLine requestLine;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(HttpRequestLine requestLine, Map<String, String> headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {
        HttpRequestLine requestLine = new HttpRequestLine(bufferedReader.readLine());
        Map<String, String> headers = parseHttpHeaders(bufferedReader);
        String body = parseBody(bufferedReader, headers);
        return new HttpRequest(requestLine, headers, body);
    }

    private static String parseBody(BufferedReader bufferedReader, Map<String, String> headers)
            throws IOException {
        int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
        return readData(bufferedReader, contentLength);
    }

    private static Map<String, String> parseHttpHeaders(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        Map<String, String> headers = new HashMap<>();
        while (!isNullOrEmpty(line)) {
            String[] tokens = line.split(":");
            headers.put(tokens[0], tokens[1].trim());
            line = bufferedReader.readLine();
        }
        return headers;
    }

    private static boolean isNullOrEmpty(String line) {
        return line == null || "".equals(line);
    }

    public FilenameExtension getFilenameExtension() {
        String path = getPath();
        String[] splitPath = path.split("\\.");
        String extension = splitPath[splitPath.length - 1];
        return FilenameExtension.from(extension);
    }

    public Map<String, String> getBodyLikeQueryParams() {
        return HttpRequestLine.parseQueryParams(body);
    }

    public HttpRequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQueryParams() {
        return requestLine.getQueryParams();
    }

    public String getBody() {
        return body;
    }
    public HttpMethod getHttpMethod() {
        return requestLine.getHttpMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHttpVersion() {
        return requestLine.getHttpVersion();
    }
}
