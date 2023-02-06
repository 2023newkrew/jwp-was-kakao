package webserver.response;

import org.springframework.http.HttpStatus;
import webserver.request.Request;

import java.nio.charset.StandardCharsets;

public class Response {

    private HttpStatus status;
    private byte[] body;
    private String contentType;
    private String version;
    private String location;

    public Response(Request request) {
        version = request.getVersion();
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
                "%s %d %s \r\nContent-Type: %s;charset=utf-8 \r\nContent-Length: %d \r\n"
                , version, status.value(), status.name(), contentType, body.length
        );
        if(location != null) {
            header += String.format("Location: %s\r\n", location);
        }
        return header += "\r\n";
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", body=" + new String(body, StandardCharsets.UTF_8) +
                ", contentType='" + contentType + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
