package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import org.springframework.http.HttpStatus;
import webserver.FilenameExtension;

public class HttpResponse {
    private final HttpStatus status;
    private final HttpResponseHeader header;
    private final byte[] body;

    public HttpResponse(HttpStatus status, HttpResponseHeader header, byte[] body) {
        this.status = status;
        this.header = header;
        this.body = body;
    }

    public HttpResponse(HttpStatus status) {
        this(status, new HttpResponseHeader(), status.getReasonPhrase().getBytes());
    }

    private HttpResponse(HttpResponseBuilder builder) {
        this(builder.status, builder.header, builder.body);
    }

    public static HttpResponse ok(byte[] body, FilenameExtension extension) {
        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .contentType(extension.getContentType())
                .contentLength(body.length)
                .body(body)
                .build();

    }
    public static HttpResponse found(byte[] body, FilenameExtension extension, String location) {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .contentType(extension.getContentType())
                .contentLength(body.length)
                .location(location)
                .body(body)
                .build();
    }

    private static HttpResponseHeader createHeader(byte[] body, FilenameExtension extension) {
        HttpResponseHeader header = new HttpResponseHeader();
        header.setContentType(extension.getContentType());
        header.setContentLength(body.length);
        return header;
    }

    public static HttpResponse internalServerError() {
        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().getBytes())
                .build();
    }

    public void writeResponse(DataOutputStream dos) throws IOException{
        dos.writeBytes(getResponseLine());
        for (Entry<String, String> entry : header.entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
        }
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private String getResponseLine() {
        return "HTTP/1.1 " + status.value() + " " + status.name() + " \r\n";
    }

    public void setCookie(String key, String value) {
        header.setCookie(key, value);
    }

    public static HttpResponseBuilder builder() {
        return new HttpResponseBuilder();
    }

    private static class HttpResponseBuilder {

        private HttpStatus status;
        private HttpResponseHeader header;
        private byte[] body;

        private HttpResponseBuilder(HttpStatus status, HttpResponseHeader header, byte[] body) {
            this.status = status;
            this.header = header;
            this.body = body;
        }

        public HttpResponseBuilder() {
            this.header = new HttpResponseHeader();
        }

        public HttpResponseBuilder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public HttpResponseBuilder body(byte[] body){
            this.body = body;
            return this;
        }

        public HttpResponseBuilder header(HttpResponseHeader header) {
            this.header = header;
            return this;
        }

        public HttpResponseBuilder setCookie(String key, String value) {
            this.header.setCookie(key, value);
            return this;
        }

        public HttpResponseBuilder contentType(String contentType) {
            this.header.setContentType(contentType);
            return this;
        }

        public HttpResponseBuilder location(String location) {
            this.header.setLocation(location);
            return this;
        }

        public HttpResponseBuilder contentLength(int contentLength) {
            this.header.setContentLength(contentLength);
            return this;
        }

        public HttpResponse build() {
            if (this.body == null) {
                this.body = new byte[0];
            }
            return new HttpResponse(this);
        }
    }
}
