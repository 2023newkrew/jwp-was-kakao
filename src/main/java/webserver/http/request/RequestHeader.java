package webserver.http.request;

import org.springframework.http.HttpMethod;
import webserver.exception.InvalidRequestHeaderException;
import webserver.http.Cookie;
import webserver.http.Session;
import webserver.http.SessionManager;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHeader {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String ACCEPT = "Accept";
    private static final String COOKIE = "Cookie";

    private HttpMethod httpMethod;
    private String url;
    private String protocol;
    private String contentType;
    private int contentLength;
    private String accept = "text/html";
    private Cookie cookies;

    public RequestHeader(List<String> headerLines) {
        if (headerLines == null || headerLines.size() < 1) {
            throw new InvalidRequestHeaderException();
        }
        extractInfoFromFirstLine(URLDecoder.decode(headerLines.remove(0), StandardCharsets.UTF_8));
        if (headerLines.size() > 0) {
            extractInfoFromRemainLines(headerLines);
        }
    }

    private void extractInfoFromFirstLine(String firstLine) {
        String[] tokens = firstLine.split(" ");

        if (tokens.length < 3 || (httpMethod = HttpMethod.resolve(tokens[0])) == null) {
            throw new InvalidRequestHeaderException();
        }

        url = tokens[1];
        protocol = tokens[2];
    }


    private void extractInfoFromRemainLines(List<String> headerLines) {
        Map<String, String> headerInfoMap = new HashMap<>();
        headerLines.stream()
                .map(line -> line.split(":"))
                .filter((tokens) -> tokens.length == 2)
                .forEach((tokens) -> {
                    headerInfoMap.put(tokens[0], tokens[1].trim());
                });

        String contentLengthString = headerInfoMap.get(CONTENT_LENGTH);
        if (contentLengthString != null) {
            contentLength = Integer.parseInt(contentLengthString);
        }
        String acceptValue = headerInfoMap.get(ACCEPT);
        if (acceptValue != null) {
            accept = acceptValue.split(",")[0];
        }
        contentType = headerInfoMap.get(CONTENT_TYPE);
        String cookieString = headerInfoMap.get(COOKIE);
        cookies = new Cookie(cookieString);
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

    public Cookie getCookies() {
        return cookies;
    }

    public Session getSession() {
        return SessionManager.getSession(cookies.getCookie(SessionManager.SESSION_ID_NAME));
    }

    public boolean hasSessionId() {
        return cookies.hasSessionId();
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "httpMethod=" + httpMethod +
                ", url='" + url + '\'' +
                ", protocol='" + protocol + '\'' +
                ", contentType='" + contentType + '\'' +
                ", contentLength=" + contentLength +
                ", accept='" + accept + '\'' +
                '}';
    }
}
