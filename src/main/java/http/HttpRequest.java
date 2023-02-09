package http;

import java.util.List;
import java.util.Objects;

public class HttpRequest {
    private HttpMethod method;
    private String URL;
    private String version;
    private HttpRequestParams parameters;
    private HttpHeaders headers;
    private String body;

    public HttpRequest() {
        parameters = new HttpRequestParams();
        headers = new HttpHeaders();
    }

    public HttpRequest(HttpMethod method, String URL, String version, HttpHeaders headers) {
        this(method, URL, version, headers, "");
    }

    public HttpRequest(HttpMethod method, String URL, String version, HttpHeaders headers, String body) {
        this(method, URL, version, new HttpRequestParams(), headers, body);
    }

    public HttpRequest(HttpMethod method, String URL, String version, HttpRequestParams parameters, HttpHeaders headers) {
        this(method, URL, version, parameters, headers, "");
    }

    public HttpRequest(HttpMethod method, String URL, String version, HttpRequestParams parameters, HttpHeaders headers, String body) {
        this.method = method;
        this.URL = URL;
        this.version = version;
        this.parameters = parameters;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getURL() {
        return URL;
    }

    public String getVersion() {
        return version;
    }

    public HttpRequestParams getParameters() {
        return parameters;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setParameters(HttpRequestParams parameters) {
        this.parameters = parameters;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addParameters(HttpRequestParams parameters) {
        this.parameters.setParameters(parameters.getParameters());
    }

    public void addParameter(String name, String value) {
        this.parameters.setParameter(name, value);
    }

    public void addHeaders(HttpHeaders headers) {
        this.headers.setHeaders(headers.getHeaders());
    }

    public void addHeader(String name, List<String> value) {
        this.headers.setHeader(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest request = (HttpRequest) o;
        return method == request.method &&
                Objects.equals(URL, request.URL) &&
                Objects.equals(version, request.version) &&
                Objects.equals(parameters, request.parameters) &&
                Objects.equals(headers, request.headers) &&
                Objects.equals(body, request.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, URL, version, parameters, headers, body);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", URL='" + URL + '\'' +
                ", version='" + version + '\'' +
                ", parameters=" + parameters +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
