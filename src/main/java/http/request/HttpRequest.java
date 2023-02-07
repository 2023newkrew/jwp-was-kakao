package http.request;

import http.HttpHeader;
import http.HttpMethod;
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
        return requestLine.getRequestUri().getRequestParam();
    }

    public RequestParam getRequestBodyParam() {
        return requestBody.getRequestParam();
    }

    public HttpHeader getRequestHeader() {
        return httpHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
