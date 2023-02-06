package http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final HttpResponseHeader header;
    private final HttpResponseBody body;

    public HttpResponse(HttpResponseHeader header, HttpResponseBody body) {
        this.header = header;
        this.body = body;
    }

    public static class Builder {
        private String httpVersion = "1.1";
        private HttpStatus status = HttpStatus.OK;
        private byte[] body = {};
        private Map<HttpHeaders, String> attributes;

        public Builder() {
            this.attributes = new HashMap<>();
        }

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder httpVersion(String httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public Builder addAttribute(HttpHeaders key, String value) {
            this.attributes.put(key, value);
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            this.attributes.put(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length));
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(
                    new HttpResponseHeader(this.httpVersion, this.status, this.attributes),
                    new HttpResponseBody(this.body)
            );
        }
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(header.getResponseHeader());
        bos.write(body.getBody());

        return bos.toByteArray();
    }

}
