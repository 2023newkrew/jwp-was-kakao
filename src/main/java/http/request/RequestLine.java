package http.request;

import http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestLine {
    private static final String DELIMITER = " ";
    private static final int VALID_REQUEST_PARAMS_LENGTH = 3;
    private static final int INDEX_OF_METHOD = 0;
    private static final int INDEX_OF_PATH = 1;
    private static final int INDEX_OF_VERSION = 2;

    private final HttpMethod httpMethod;
    private final RequestUri requestUri;
    private final String httpVersion;

    public RequestLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        String[] params = line.split(DELIMITER);
        validate(params);
        httpMethod = HttpMethod.valueOf(params[INDEX_OF_METHOD]);
        requestUri = new RequestUri(params[INDEX_OF_PATH]);
        httpVersion = params[INDEX_OF_VERSION];
    }

    private void validate(String[] params) {
        if (params.length != VALID_REQUEST_PARAMS_LENGTH) {
            throw new IllegalArgumentException();
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public RequestUri getRequestURI() {
        return requestUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
