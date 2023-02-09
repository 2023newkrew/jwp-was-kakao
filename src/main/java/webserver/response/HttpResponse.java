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

    public static HttpResponse ok(byte[] body, FilenameExtension extension) {
        return new HttpResponse(HttpStatus.OK, createHeader(body, extension), body);
    }
    public static HttpResponse found(byte[] body, FilenameExtension extension, String location) {
        HttpResponseHeader header = createHeader(body, extension);
        header.setLocation(location);
        return new HttpResponse(HttpStatus.FOUND, header, body);
    }

    private static HttpResponseHeader createHeader(byte[] body, FilenameExtension extension) {
        HttpResponseHeader header = new HttpResponseHeader();
        header.setContentType(extension.getContentType());
        header.setContentLength(body.length);
        return header;
    }

    public static HttpResponse internalServerError() {
        return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
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
}
