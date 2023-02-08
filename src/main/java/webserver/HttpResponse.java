package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private final OutputStream outputStream;
    private final ResponseWriter responseWriter;

    private HttpStatus status = HttpStatus.OK;
    private String contentType;
    private byte[] body = new byte[0];

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.responseWriter = new ResponseWriter();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void sendResponse() {
        responseWriter.write(Map.of("Content-Type", contentType, "Content-Length", String.valueOf(body.length)));
    }

    public void sendRedirect(String location) {
        status = HttpStatus.FOUND;
        responseWriter.write(Map.of("Location", location));
    }

    public void sendNotFound() {
        status = HttpStatus.NOT_FOUND;
        responseWriter.write();
    }

    public void sendError(Exception e) {
        status = HttpStatus.INTERNAL_SERVER_ERROR;
        body = e.getMessage().getBytes();
        responseWriter.write();
    }

    private class ResponseWriter {
        private final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        private void write() {
            write(new HashMap<>());
        }

        private void write(Map<String, String> headers) {
            try {
                dataOutputStream.writeBytes("HTTP/1.1 " + status.getValue() + " " + status.getReasonPhrase() + " \r\n");
                headers.forEach(this::writeHeader);
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.write(body);
                dataOutputStream.flush();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        private void writeHeader(String name, String value) {
            try {
                dataOutputStream.writeBytes(name + ": " + value + " \r\n");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
