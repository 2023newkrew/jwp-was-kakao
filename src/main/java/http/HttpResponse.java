package http;

import java.util.List;

public class HttpResponse {
    private HttpStatus status;
    private String version;
    private HttpHeaders headers;
    private byte[] body;

    public HttpResponse() {
        headers = new HttpHeaders();
        body = new byte[0];
    }

    public HttpResponse(HttpStatus status, String version, HttpHeaders headers) {
        this(status, version, headers, new byte[0]);
    }

    public HttpResponse(HttpStatus status, String version, HttpHeaders headers, byte[] body) {
        this.status = status;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void addHeaders(HttpHeaders headers) {
        this.headers.setHeaders(headers.getHeaders());
    }

    public void addHeader(String name, List<String> value) {
        this.headers.setHeader(name, value);
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
        headers.getHeaderNames()
                .forEach(headerName -> {
                    String values = String.join(",", headers.getHeader(headerName));
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
}
