package webserver.request;

import static utils.IOUtils.readData;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpMethod;
import webserver.FilenameExtension;

public class HttpRequest {
    private final HttpRequestLine requestLine;
    private final HttpRequestHeader headers;
    private final String body;

    public HttpRequest(HttpRequestLine requestLine, HttpRequestHeader headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {
        HttpRequestLine requestLine = HttpRequestLine.parse(bufferedReader);
        HttpRequestHeader headers = HttpRequestHeader.parse(bufferedReader);
        String body = readData(bufferedReader, headers.getContentLength());
        return new HttpRequest(requestLine, headers, body);
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

    public HttpRequestHeader getHeaders() {
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
