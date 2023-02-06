package webserver.request;

import org.springframework.http.HttpMethod;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHeader {
    private static final String HOST = "Host";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String ACCEPT = "Accept";

    private HttpMethod httpMethod;
    private String path;
    private Map<String, String> queryParameterMap = new HashMap<>();
    private String version;
    private String host;
    private String contentType;
    private int contentLength;
    private String accept = "text/html";

    public RequestHeader(List<String> headerLines) {
        if (headerLines == null || headerLines.size() < 1) {
            throw new RuntimeException("EmptyRequestHeaderException");
        }
        extractInfoFromFirstLine(URLDecoder.decode(headerLines.remove(0), StandardCharsets.UTF_8));
        if (headerLines.size() > 0) {
            extractInfoFromRemainLines(headerLines);
        }
    }

    private void extractInfoFromFirstLine(String firstLine) {
        String[] tokens = firstLine.split(" ");

        if (tokens.length < 3) {
            throw new RuntimeException("InvalidHttpRequestHeaderException");
        }

        if ((httpMethod = HttpMethod.resolve(tokens[0])) == null) {
            throw new RuntimeException("InvalidHttpRequestHeaderException");
        }

        String[] pathToken = tokens[1].split("\\?");
        path = pathToken[0];
        if (pathToken.length > 1) {
            setQueryParameters(pathToken[1]);
        }

        version = tokens[2];
    }

    private void setQueryParameters(String queryString) {
        String[] tokens = queryString.split("&");
        for (String token : tokens) {
            String[] keyValue = token.split("=");
            if (keyValue.length != 2) {
                throw new RuntimeException("InvalidQueryStringException");
            }
            queryParameterMap.put(keyValue[0], keyValue[1]);
        }
    }

    private void extractInfoFromRemainLines(List<String> headerLines) {
        Map<String, String> headerInfoMap = new HashMap<>();
        for (String line : headerLines) {
            String[] tokens = line.split(":");
            if (tokens.length < 2) continue;
            headerInfoMap.put(tokens[0], tokens[1].trim());
        }

        host = headerInfoMap.get(HOST);

        String contentLengthString = headerInfoMap.get(CONTENT_LENGTH);
        if (contentLengthString != null) {
            contentLength = Integer.parseInt(contentLengthString);
        }
        String acceptValue =  headerInfoMap.get(ACCEPT);
        if(acceptValue != null) {
            accept = acceptValue.split(",")[0];
        }
        contentType = headerInfoMap.get(CONTENT_TYPE);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getAccept() {
        return accept;
    }

    public String getContentType() {
        return contentType;
    }

    public boolean hasStaticPath() {
        if(path == null) return false;
        String[] pathToken = path.split("/");
        if(pathToken.length < 2) return false;
        return StaticDirectory.resolve(pathToken[1].toUpperCase()) != null;
    }

    public String getQueryParam(String key) {
        return queryParameterMap.get(key);
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "httpMethod=" + httpMethod +
                ", path='" + path + '\'' +
                ", queryParameterMap=" + queryParameterMap +
                ", version='" + version + '\'' +
                ", host='" + host + '\'' +
                ", contentLength=" + contentLength +
                ", accept='" + accept + '\'' +
                '}';
    }

}
