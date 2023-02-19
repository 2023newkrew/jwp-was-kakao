package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;

public class Response {
    private final String PROTOCOL = "HTTP";
    private final String VERSION = "1.1";
    private HttpStatus status = HttpStatus.OK;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private final Map<String, String> cookies = new HashMap<>();
    private final DataOutputStream dos;
    private byte[] body = null;

    public Response(DataOutputStream dos) {
        this.dos = dos;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setCookie(String key, String value) {
        cookies.put(key, value);
    }

    public void setBody(byte[] body, String type) {
        this.body = body;
        headers.put("Content-Type", type + ";charset=utf-8");
        headers.put("Content-Length", String.valueOf(body.length));
    }

    public void found(URI location) {
        this.status = HttpStatus.FOUND;
        headers.put("Location", location.toString());
    }

    public void send() throws IOException {
        writeHeader();
        writeBody();
        dos.flush();
    }

    private void writeBody() throws IOException {
        if (Objects.isNull(body)) {
            return;
        }
        String bodySize = Objects.nonNull(headers.get("Content-Length")) ? headers.get("Content-Length") : "0";
        dos.write(body, 0, Integer.parseInt(bodySize));
    }

    private void writeHeader() throws IOException {
        dos.writeBytes(PROTOCOL + "/" + VERSION + " " + status + " \r\n");
        writeHeaderProperties();
        writeCookies();
        dos.writeBytes("\r\n");
    }

    private void writeHeaderProperties() {
        headers.forEach((key, value) -> {
            try {
                dos.writeBytes(key + ": " + value + " \r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void writeCookies() {
        cookies.forEach((key, value) -> {
            try {
                dos.writeBytes("Set-Cookie: " + key + "=" + value + " \r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
