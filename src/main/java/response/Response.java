package response;

import request.HttpCookie;

import java.io.ByteArrayOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Response {

    private final String header;

    private final byte[] body;

    private Response(String header, byte[] body) {
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
        private String setCookie;
        private byte[] body;

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
            this.body = body.getBytes();
            contentLength(this.body.length);
            return this;
        }

        public ResponseBuilder body(byte[] body) {
            this.body = body;
            contentLength(this.body.length);
            return this;
        }

        public ResponseBuilder setCookie(HttpCookie cookie) {
            this.setCookie = "Set-Cookie: " + cookie.toString();
            return this;
        }

        public Response build() {
            if (body == null) {
                contentLength(0);
            }
            String header = Stream.of(status, contentType, location, setCookie, contentLength)
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
        return new ResponseBuilder("HTTP/1.1 404 Not Found");
    }

    public static ResponseBuilder found() {
        return new ResponseBuilder("HTTP/1.1 302 Found");
    }

    public static ResponseBuilder unauthorized() {
        return new ResponseBuilder("HTTP/1.1 401 Unauthorized");
    }

    public byte[] getBytes() {
        if (body == null) {
            return header.getBytes();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String tempHeader = this.header + "\r\n\r\n";
        outputStream.write(tempHeader.getBytes(), 0, tempHeader.getBytes().length);
        outputStream.write(body, 0, body.length);
        return outputStream.toByteArray();
    }
}
