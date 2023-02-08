package webserver.response;

import webserver.constant.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpStatus status;
    private byte[] body;
    private Map<String, String> headers;

    private HttpResponse(Builder builder) {
        this.status = builder.status;
        this.body = builder.body;
        this.headers = builder.headers;
    }

    public static class Builder {
        private HttpStatus status;
        private byte[] body;
        private Map<String, String> headers = new HashMap<>();

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder setBody(byte[] body) {
            this.body = body;
            headers.put("Content-Length", Integer.toString(body.length));
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    public void send(DataOutputStream out) throws IOException {
        out.writeBytes("HTTP/1.1 " + status.getStatusCode() + " " + status.getStatusMessage() + "\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            out.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
        out.writeBytes("\r\n");
        if (body != null) {
            out.write(body);
        }
        out.flush();
    }
}
