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

    public static HttpResponse of(HttpStatus status, ContentType contentType, String body) {
        return new HttpResponse(status, HttpResponseHeader.of(status, contentType, body.getBytes().length), body);
    }

    public static HttpResponse create302FoundResponse(String redirectURI) {
        return new HttpResponse(HttpStatus.FOUND, HttpResponseHeader.create302FoundHeader(redirectURI), null);
    }

    public HttpResponseHeader getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public void writeToOutputStream(DataOutputStream dos) throws IOException {
        writeHeaderToOutputStream(dos);
        writeBodyToOutputStream(dos);
    }

    private void writeHeaderToOutputStream(DataOutputStream dos) throws IOException {
        for (String header : headers.getHeaders()) {
            dos.writeBytes(header + " \r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void writeBodyToOutputStream(DataOutputStream dos) throws IOException {
        dos.write(body.getBytes(), 0, body.getBytes().length);
        dos.flush();
    }
}
