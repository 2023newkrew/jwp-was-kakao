package webserver;

import enums.ContentType;
import org.springframework.http.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {

    private HttpStatus status;
    private HttpResponseHeader headers;
    private String body;

    private HttpResponse(HttpStatus status, HttpResponseHeader headers, String body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public static BodyBuilder status(HttpStatus status) {
        return new BodyBuilder(status);
    }

    public static HttpResponse body(String body) {
        return new BodyBuilder(HttpStatus.OK)
                .contentType(ContentType.JSON)
                .body(body);
    }

    public void writeToOutputStream(DataOutputStream dos) throws IOException {
        writeStatusToOutputStream(dos);
        writeHeaderToOutputStream(dos);
        writeBodyToOutputStream(dos);
    }

    private void writeStatusToOutputStream(DataOutputStream dos) throws IOException {
        dos.writeBytes(String.format("HTTP/1.1 %d %s \r\n", this.status.value(), this.status.name()));
    }

    private void writeHeaderToOutputStream(DataOutputStream dos) throws IOException {
        for (var header : headers.getHeaders().entrySet()) {
            dos.writeBytes(String.format("%s: %s \r\n", header.getKey(), header.getValue()));
        }
        dos.writeBytes("\r\n");
    }

    private void writeBodyToOutputStream(DataOutputStream dos) throws IOException {
        if (this.body != null) {
            dos.write(body.getBytes(), 0, body.getBytes().length);
        }
        dos.flush();
    }

    public static class BodyBuilder {
        private final HttpStatus status;
        private final HttpResponseHeader headers;

        public BodyBuilder(HttpStatus status) {
            this.status = status;
            this.headers = HttpResponseHeader.emptyHeader();
        }

        public BodyBuilder(HttpStatus status, HttpResponseHeader headers) {
            this.status = status;
            this.headers = headers;
        }

        public BodyBuilder contentType(ContentType contentType) {
            this.headers.setContentType(contentType);
            return this;
        }

        public BodyBuilder location(String location) {
            this.headers.setLocation(location);
            return this;
        }

        public HttpResponse body(String body) {
            this.headers.setContentLength(body.getBytes().length);
            return new HttpResponse(status, headers, body);
        }

        public HttpResponse build() {
            return new HttpResponse(status, headers, null);
        }
    }
}
