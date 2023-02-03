package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpRequest {
    private final static Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private final RequestLine requestLine;
    private final RequestHeader requestHeader;
    private final RequestBody requestBody;

    public HttpRequest(BufferedReader reader) {
        try {
            requestLine = new RequestLine(reader.readLine());
            requestHeader = new RequestHeader(reader);
            requestBody = new RequestBody(reader);
        } catch (Exception e) {
            logger.error("HttpRequest Error", e);
            throw new IllegalArgumentException();
        }
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
