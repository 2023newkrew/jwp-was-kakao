package webserver.request;

import org.springframework.http.HttpMethod;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHeader {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String ACCEPT = "Accept";

    private HttpMethod httpMethod;
    private String url;
    private String protocol;
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

        if (tokens.length < 3 || (httpMethod = HttpMethod.resolve(tokens[0])) == null) {
            throw new RuntimeException("InvalidHttpRequestHeaderException");
        }

        url = tokens[1];
        protocol = tokens[2];
    }


    private void extractInfoFromRemainLines(List<String> headerLines) {
        Map<String, String> headerInfoMap = new HashMap<>();
        for (String line : headerLines) {
            String[] tokens = line.split(":");
            if (tokens.length < 2) continue;
            headerInfoMap.put(tokens[0], tokens[1].trim());
        }

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

    public String getURL() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getAccept() {
        return accept;
    }
}
