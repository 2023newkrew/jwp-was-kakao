package webserver.http;

import http.HttpHeaders;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpRequestParams;
import utils.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestReader implements Closeable {

    private final BufferedReader br;
    private final HttpRequestLineParser httpRequestLineParser;
    private final HttpRequestHeaderParser httpRequestHeaderParser;

    private HttpMethod httpMethod;
    private String url;
    private HttpRequestParams parameters;
    private String httpVersion;
    private HttpHeaders headers;

    public HttpRequestReader(InputStream in) {
        this.br = new BufferedReader(new InputStreamReader(in));
        this.httpRequestLineParser = new HttpRequestLineParser();
        this.httpRequestHeaderParser = new HttpRequestHeaderParser();
    }

    public HttpRequest readHttpRequest() {
        String requestLine = readRequestLine();
        if (requestLine == null || requestLine.isBlank()) {
            return null;
        }
        parseRequestLine(requestLine);

        List<String> headerLines = readHeaderLines();
        parseHeader(headerLines);

        String body = readBody();

        return new HttpRequest(httpMethod, url, httpVersion, parameters, headers, body);
    }

    private String readBody() {
        Integer contentLength = getContentLength();
        if (contentLength == null) {
            return "";
        }

        try {
            return IOUtils.readData(br, contentLength);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Integer getContentLength() {
        String headerName = headers.getHeaderNames().stream()
                .filter(header -> header.equalsIgnoreCase("content-length"))
                .findFirst()
                .orElse(null);

        if (headerName == null) {
            return null;
        }

        return Integer.parseInt(headers.getHeader(headerName).get(0));
    }

    private void parseHeader(List<String> headerLines) {
        headers = httpRequestHeaderParser.parse(headerLines);
    }

    private List<String> readHeaderLines() {
        List<String> headerLines = new ArrayList<>();

        String line;
        try {
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                headerLines.add(line);
            }
            return headerLines;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void parseRequestLine(String requestLine) {
        httpMethod = httpRequestLineParser.extractHttpMethod(requestLine);
        url = httpRequestLineParser.extractUrl(requestLine);
        parameters = httpRequestLineParser.extractParams(requestLine);
        httpVersion = httpRequestLineParser.extractHttpVersion(requestLine);
    }

    private String readRequestLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws IOException {
        br.close();
    }
}
