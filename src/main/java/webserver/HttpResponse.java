package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.http.HttpStatus;

public class HttpResponse {
    private final HttpStatus status;
    private final Map<String, String> header;
    private final byte[] body;

    public HttpResponse(HttpStatus status, Map<String, String> header, byte[] body) {
        this.status = status;
        this.header = header;
        this.body = body;
    }

    public static HttpResponse ok(byte[] body, FilenameExtension extension) {
        return new HttpResponse(HttpStatus.OK, createHeader(body, extension), body);
    }
    public static HttpResponse found(byte[] body, FilenameExtension extension, String location) {
        Map<String, String> header = createHeader(body, extension);
        header.put("Location", location);
        return new HttpResponse(HttpStatus.FOUND, header, body);
    }

    private static Map<String, String> createHeader(byte[] body, FilenameExtension extension) {
        Map<String, String> header = new LinkedHashMap<>();
        header.put("Content-Type", extension.getContentType() + ";charset=utf-8");
        if (body.length > 0) {
            header.put("Content-Length", String.valueOf(body.length));
        }
        return header;
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

    public void setCookie(String cookie) {
        header.put("Set-Cookie", cookie);
    }
}
