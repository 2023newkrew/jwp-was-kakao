package webserver.request;

import webserver.common.HttpHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestHeader {

    private static final String REQUEST_HEADER_KEY_VALUE_DELIMITER = ":";

    private final Map<HttpHeader, String> requestHeaders;

    private RequestHeader(Map<HttpHeader, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public static RequestHeader parse(BufferedReader reader) throws IOException {
        Map<HttpHeader, String> requestHeader = new HashMap<>();
        String header;
        while (!Objects.equals(header = reader.readLine(), "")) {
            String[] headerInformation = header.split(REQUEST_HEADER_KEY_VALUE_DELIMITER);
            HttpHeader httpHeader = HttpHeader.of(headerInformation[0]);
            String value = headerInformation[1].trim();
            requestHeader.put(httpHeader, value);
        }
        return new RequestHeader(requestHeader);
    }

    public HttpCookie getCookie() {
        if (requestHeaders.containsKey(HttpHeader.COOKIE)) {
            String cookieValue = requestHeaders.get(HttpHeader.COOKIE);
            return HttpCookie.of(cookieValue);
        }
        return new HttpCookie();
    }

    public boolean checkRequestBodyExists() {
        return requestHeaders.containsKey(HttpHeader.CONTENT_LENGTH);
    }

    public int findRequestBodySize() {
        return Integer.parseInt(requestHeaders.get(HttpHeader.CONTENT_LENGTH));
    }
}
