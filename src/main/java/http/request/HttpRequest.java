package http.request;

import http.Cookie;
import http.HttpHeader;
import http.HttpMethod;
import http.session.Session;
import http.session.SessionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;

import java.io.BufferedReader;
import java.util.Optional;

public class HttpRequest {
    private final static Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private final RequestLine requestLine;
    private final HttpHeader httpHeader;
    private final RequestBody requestBody;

    public HttpRequest(BufferedReader reader) {
        try {
            requestLine = new RequestLine(reader);
            httpHeader = new HttpHeader(reader);
            requestBody = new RequestBody(IOUtils.readData(reader, httpHeader.getContentLength()));
        } catch (Exception e) {
            logger.error("HttpRequest Error", e);
            throw new IllegalArgumentException();
        }
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public HttpMethod getMethod() {
        return requestLine.getHttpMethod();
    }

    public RequestParam getRequestParam() {
        HttpMethod method = getMethod();
        if (method == HttpMethod.GET) {
            return requestLine.getRequestURI().getRequestParam();
        }
        if (method == HttpMethod.POST) {
            return requestBody.getRequestParam();
        }
        return RequestParam.empty();
    }

    public HttpHeader getRequestHeader() {
        return httpHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public String getPath() {
        return getRequestLine().getRequestURI().getPath();
    }

    public boolean isLoggedIn() {
        return getLoggedInUser().isPresent();
    }

    public Optional<User> getLoggedInUser() {
        if (!httpHeader.getCookieList().hasCookie(SessionManager.SESSION_ID_NAME)) return Optional.empty();
        Cookie cookie = (Cookie) httpHeader.getCookieList().getCookie(SessionManager.SESSION_ID_NAME);
        Optional<Session> session = SessionManager.findSession(cookie.getValue());
        return session.map(value -> (User) value.getAttribute(SessionManager.USER_NAME));
    }
}
