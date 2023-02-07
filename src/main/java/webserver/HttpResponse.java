package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private final HttpStatusCode httpStatusCode;
    private final Map<String, String> header;
    private final byte[] body;

    public HttpResponse(HttpStatusCode httpStatusCode, byte[] body) {
        this.httpStatusCode = httpStatusCode;
        this.header = new LinkedHashMap<>();
        this.body = body;
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public void response(DataOutputStream dos) throws IOException {
        writeHeader(dos);
        if (body.length > 0) {
            writeBody(dos);
        }

        dos.flush();
    }

    private void writeHeader(DataOutputStream dos) throws IOException {
        dos.writeBytes(HTTP_VERSION + " " + httpStatusCode.getStatusCode() + " " + httpStatusCode.getMessage() + " \r\n");
        header.forEach((key, value) -> {
            try {
                dos.writeBytes(key + ": " + value + " \r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        dos.writeBytes("\r\n");
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }
}
