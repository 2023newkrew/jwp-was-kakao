package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
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
        try {
            dos.writeBytes(getResponseLine());
            header.forEach((key, value) -> writeLine(dos, key + ": " + value + " \r\n"));
            dos.writeBytes("\r\n");

            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    
    private void writeLine(DataOutputStream dos, String line) {
        try {
            dos.writeBytes(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getResponseLine() {
        return "HTTP/1.1 " + status.value() + " " + status.name() + " \r\n";
    }
}
