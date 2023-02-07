package webserver.request;

import webserver.FileType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final RequestLine requestLine;
    private final RequestHeader requestHeader;
    private final RequestBody requestBody;

    private Request(RequestLine requestLine, RequestHeader requestHeader, RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public static Request parse(BufferedReader reader) throws IOException {
        RequestLine requestLine = RequestLine.parse(reader.readLine());
        RequestHeader requestHeader = RequestHeader.parse(reader);
        RequestBody requestBody = RequestBody.parse(reader, requestHeader);
        return new Request(requestLine, requestHeader, requestBody);
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public Map<String, String> getQueryString() {
        if (requestLine.hasQueryString()) {
            return requestLine.parseQueryString();
        }
        return new HashMap<>();
    }

    public Map<String, String> getRequestBody() {
        return requestBody.parseRequestBody();
    }

    public FileType findRequestedFileType() {
        return requestLine.findRequestedFileType();
    }

    public Method getMethod() {
        return requestLine.getMethod();
    }
}
