package http.request;

import http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            requestBody = new RequestBody(reader);
        } catch (Exception e) {
            logger.error("HttpRequest Error", e);
            throw new IllegalArgumentException();
        }
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public HttpHeader getRequestHeader() {
        return httpHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
