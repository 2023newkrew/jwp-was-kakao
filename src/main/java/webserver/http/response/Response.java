package webserver.http.response;

import org.springframework.http.HttpStatus;
import webserver.http.Cookie;
import webserver.http.request.Request;

public class Response {

    private HttpStatus status;
    private String contentType;
    private String protocol;
    private String location;
    private Cookie cookies;
    private byte[] body;

    public Response(Request request) {
        protocol = request.getProtocol();
        contentType = request.getAccept();
        body = new byte[]{};
        cookies = new Cookie();
    }

    public byte[] getBody() {
        return body;
    }

    public Cookie getCookies() {
        return this.cookies;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setRedirection(String location) {
        this.location = location;
        this.status = HttpStatus.FOUND;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
        if(!cookies.isEmpty()) {
            header += String.format("%s \r\n", cookies.toResponseHeaderLine());
        }
        return header += "\r\n";
    }

}
