package http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private final String statusLine;
    private final String headers;
    private final byte[] body;

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public Response(String statusLine, String headers, byte[] body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public String getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void send(DataOutputStream dos) {
        try {
            dos.writeBytes(statusLine + " \r\n");
            dos.writeBytes(headers);
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
