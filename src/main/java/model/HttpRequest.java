package model;

import utils.IOUtils;
import webserver.constants.method.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final String uri;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final String body;

    private HttpRequest(HttpMethod httpMethod, String uri, String httpVersion, Map<String, String> headers, String body) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public String getBody() {
        return body;
    }

    public static HttpRequest parse(InputStream in) throws IOException {
        final String HEADER_SEPARATOR = ": ";
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line = br.readLine();
        String[] lineParts = line.split(" ");

        HttpMethod httpMethod = HttpMethod.valueOf(lineParts[0]);
        String uri = lineParts[1];
        String httpVersion = lineParts[2];

        Map<String, String> headers = new HashMap<>();
        String body = "";

        while ((line = br.readLine()) != null && line.length() > 0) {
            int headerSeparatorIndex = line.indexOf(HEADER_SEPARATOR);
            String headerName = line.substring(0, headerSeparatorIndex);
            String headerValue = line.substring(headerSeparatorIndex + HEADER_SEPARATOR.length());
            headers.put(headerName, headerValue);
        }

        if (headers.containsKey("Content-Length")) {
            body = URLDecoder.decode(IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length"))), "utf-8");
        }

        return new HttpRequest(httpMethod, uri, httpVersion, headers, body);
    }
}
