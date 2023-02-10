package http.request;

import http.HttpHeader;
import http.HttpMethod;
import http.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;

import java.io.BufferedReader;

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
        return httpHeader.getCookieList().hasCookie(SessionManager.SESSION_ID_NAME);
    }
}
