package webserver.response;

import org.springframework.http.HttpStatus;
import webserver.request.Request;

import java.nio.charset.StandardCharsets;

public class Response {

    private HttpStatus status;
    private String contentType;
    private String protocol;
    private String location;
    private byte[] body;

    public Response(Request request) {
        protocol = request.getProtocol();
        contentType = request.getAccept();
        body = new byte[]{};
    }

    public byte[] getBody() {
        return body;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeader() {
        String header = String.format(
                "%s %d %s \r\n",
                protocol, status.value(), status.name()
        );
        if(body != null && body.length > 0) {
            header += String.format(
                    "Content-Type: %s;charset=utf-8 \r\nContent-Length: %d \r\n",
                    contentType, body.length
            );
        }
        if(location != null) {
            header += String.format("Location: %s \r\n", location);
        }
        return header += "\r\n";
    }
}
