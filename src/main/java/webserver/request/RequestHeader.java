package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestHeader {

    private static final String REQUEST_HEADER_KEY_VALUE_DELIMITER = ":";

    private final Map<HttpHeader, String> requestHeader;

    private RequestHeader(Map<HttpHeader, String> requestHeader) {
        this.requestHeader = requestHeader;
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

    public boolean checkRequestBodyExists() {
        return requestHeader.containsKey(HttpHeader.CONTENT_LENGTH);
    }

    public int findRequestBodySize() {
        return Integer.parseInt(requestHeader.get(HttpHeader.CONTENT_LENGTH));
    }
}
