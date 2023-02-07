package http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final HttpResponseLine responseLine;
    private final HttpResponseHeader responseHeader;
    private final HttpResponseBody responseBody;

    public HttpResponse(HttpResponseLine responseLine, HttpResponseHeader responseHeader, HttpResponseBody responseBody) {
        this.responseLine = responseLine;
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
    }

    public static class Builder {
        private String httpVersion = "1.1";
        private HttpStatus status = HttpStatus.OK;
        private byte[] body = {};
        private final Map<HttpHeaders, String> headers;

        public Builder() {
            this.headers = new HashMap<>();
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
            this.headers.put(key, value);
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            this.headers.put(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length));
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(
                    new HttpResponseLine(this.httpVersion, this.status),
                    new HttpResponseHeader(this.headers),
                    new HttpResponseBody(this.body)
            );
        }
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(responseLine.getBytes());
        bos.write(responseHeader.getBytes());
        bos.write(responseBody.getBytes());

        return bos.toByteArray();
    }

}
