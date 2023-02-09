package http.request;

import http.HttpHeader;
import http.HttpSession;
import http.HttpSessions;
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
            requestLine = new RequestLine(reader.readLine());
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
        return requestLine.getRequestUri().getRequestParam();
    }

    public RequestParam getRequestBodyParam() {
        return requestBody.getRequestParam();
    }

    public String getParameter(String key) {
        if (HttpMethod.POST.equals(getMethod())) {
            return requestBody.getRequestParam().get(key);
        }
        return getRequestParam().get(key);
    }

    public String getCookie(String key) {
        return httpHeader.getCookies().getCookie(key);
    }

    public String getSessionId() {
        return getCookie(HttpSessions.SESSION_KEY);
    }

    public HttpSession getHttpSession() {
        return HttpSessions.getSession(getSessionId());
    }

    public HttpHeader getRequestHeader() {
        return httpHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
