package webserver.http;

import webserver.http.cookie.HttpCookie;
import webserver.http.cookie.HttpCookies;
import webserver.http.header.HttpHeader;
import webserver.http.header.HttpHeaders;

import java.util.Map;

public class HttpRequest {
    private final String path;
    private final HttpMethod method;
    private final HttpHeaders headers;
    private final Map<String, String> parameters;
    private final HttpCookies cookies;

    public HttpRequest(final String path, final HttpMethod method, final HttpHeaders headers, final Map<String, String> parameter, final HttpCookies cookies) {
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.parameters = parameter;
        this.cookies = cookies;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getParameter(String parameterName) {
        return parameters.get(parameterName);
    }

    public Map<String, HttpHeader> getHeaders() {
        return headers.getHeaders();
    }

    public HttpHeader getHeader(String headerName) {
        return headers.getHeader(headerName);
    }

    public Map<String, HttpCookie> getCookies() {
        return cookies.getCookies();
    }

    public HttpCookie getCookie(String cookieName) {
        return cookies.getCookie(cookieName);
    }
}
