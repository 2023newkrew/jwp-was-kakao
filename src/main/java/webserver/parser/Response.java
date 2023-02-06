package webserver.parser;

import type.ContentType;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Response {

    private final String header;

    private final String body;

    private Response(String header, String body) {
        this.header = header;
        this.body = body;
    }

    private Response(String header) {
        this.header = header;
        this.body = null;
    }

    public static class ResponseBuilder {
        private final String status;
        private String contentLength;
        private String contentType;
        private String location;
        private String body;

        public ResponseBuilder(String status) {
            this.status = status;
        }

        private void contentLength(int length) {
            this.contentLength = "Content-Length: " + length;
        }

        public ResponseBuilder contentType(ContentType contentType) {
            this.contentType = "Content-Type: " + contentType.getString();
            return this;
        }

        public ResponseBuilder location(String uri) {
            this.location = "Location: " + uri;
            return this;
        }

        public ResponseBuilder body(String body) {
            this.body = body;
            contentLength(this.body.getBytes().length);
            return this;
        }

        public Response build() {
            if (body == null) {
                contentLength(0);
            }
            String header = Stream.of(status, contentType, location, contentLength)
                    .filter(s -> s != null && !s.isEmpty())
                    .collect(Collectors.joining(" \r\n")) + " ";
            if (body == null) {
                return new Response(header);
            }
            return new Response(header, body);
        }

    }

    public static ResponseBuilder ok() {
        return new ResponseBuilder("HTTP/1.1 200 OK");
    }

    public static ResponseBuilder notFound() {
        return new ResponseBuilder("HTTP/1.1 404 Not found");
    }

    public static ResponseBuilder redirect() {
        return new ResponseBuilder("HTTP/1.1 302 Redirect");
    }

    @Override
    public String toString() {
        if (body == null) {
            return header;
        }
        return header + "\r\n\r\n" + body;
    }
}
