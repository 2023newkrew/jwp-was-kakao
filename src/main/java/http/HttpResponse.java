package http;

import enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;

@Getter
@AllArgsConstructor
public class HttpResponse {
    private HttpStatus status;
    private HttpResponseHeader headers;
    private byte[] body;

    public static HttpResponse of(HttpStatus status, ContentType contentType, byte[] body) {
        return new HttpResponse(status, HttpResponseHeader.of(status, contentType, body.length), body);
    }

    public static HttpResponse create302FoundResponse(String redirectURI) {
        return new HttpResponse(HttpStatus.FOUND, HttpResponseHeader.create302FoundHeader(redirectURI), new byte[0]);
    }

    public void writeToOutputStream(DataOutputStream dos) throws IOException {
        writeHeaderToOutputStream(dos);
        writeBodyToOutputStream(dos);
    }

    private void writeHeaderToOutputStream(DataOutputStream dos) throws IOException {
        for (String header : headers.getHeaders()) {
            dos.writeBytes(header + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void writeBodyToOutputStream(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public void addHeader(String header) {
        headers.addHeader(header);
    }
}
