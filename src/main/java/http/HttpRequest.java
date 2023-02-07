package http;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private HttpMethod method;
    private String URL;
    private String version;
    private Map<String, String> parameters;
    private Map<String, List<String>> headers;
    private String body;

    private HttpRequest() {}

    public HttpMethod getMethod() {
        return method;
    }

    public String getURL() {
        return URL;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
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

    public static final class HttpRequestBuilder {
        private HttpMethod method;
        private String URL;
        private String version;
        private Map<String, String> parameters;
        private Map<String, List<String>> headers;
        private String body;

        private HttpRequestBuilder() {
            body = "";
            parameters = Map.of();
            headers = Map.of();
        }

        public static HttpRequestBuilder aHttpRequest() {
            return new HttpRequestBuilder();
        }

        public HttpRequestBuilder withMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        public HttpRequestBuilder withURL(String URL) {
            this.URL = URL;
            return this;
        }

        public HttpRequestBuilder withVersion(String version) {
            this.version = version;
            return this;
        }

        public HttpRequestBuilder withParameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public HttpRequestBuilder withHeaders(Map<String, List<String>> headers) {
            this.headers = headers;
            return this;
        }

        public HttpRequestBuilder withBody(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.parameters = this.parameters;
            httpRequest.body = this.body;
            httpRequest.version = this.version;
            httpRequest.method = this.method;
            httpRequest.URL = this.URL;
            httpRequest.headers = this.headers;
            return httpRequest;
        }
    }
}
