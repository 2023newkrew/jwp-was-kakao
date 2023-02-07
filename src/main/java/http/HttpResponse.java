package http;

import java.util.List;
import java.util.Map;

public class HttpResponse {
    private HttpStatus status;
    private String version;
    private Map<String, List<String>> headers;
    private byte[] body;

    public HttpStatus getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] toBytes() {
        StringBuilder sb = new StringBuilder();

        byte[] statusLineAndHeaders = sb.append(generateStatusLine())
                .append(generateHeaderLines())
                .append("\r\n")
                .toString()
                .getBytes();

        if (body == null || body.length == 0) {
            return statusLineAndHeaders;
        }

        byte[] response = new byte[statusLineAndHeaders.length + body.length];
        System.arraycopy(statusLineAndHeaders, 0, response, 0, statusLineAndHeaders.length);
        System.arraycopy(body, 0, response, statusLineAndHeaders.length, body.length);

        return response;
    }

    private String generateHeaderLines() {
        if (headers == null || headers.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        headers.keySet()
                .forEach(headerName -> {
                    String values = String.join(",", headers.get(headerName));
                    sb.append(headerName).append(": ").append(values).append("\r\n");
                });

        return sb.toString();
    }

    private String generateStatusLine() {
        return version + " " +
                status.getCode() + " " +
                status.getMessage() + "\r\n";
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", body=" + new String(body) +
                '}';
    }
    public static final class HttpResponseBuilder {
        private HttpStatus status;
        private String version;
        private Map<String, List<String>> headers;

        private byte[] body;

        private HttpResponseBuilder() {
            headers = Map.of();
            body = new byte[0];
        }

        public static HttpResponseBuilder aHttpResponse() {
            return new HttpResponseBuilder();
        }

        public HttpResponseBuilder withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public HttpResponseBuilder withVersion(String version) {
            this.version = version;
            return this;
        }

        public HttpResponseBuilder withHeaders(Map<String, List<String>> headers) {
            this.headers = headers;
            return this;
        }

        public HttpResponseBuilder withBody(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.version = this.version;
            httpResponse.headers = this.headers;
            httpResponse.body = this.body;
            httpResponse.status = this.status;
            return httpResponse;
        }
    }
}
