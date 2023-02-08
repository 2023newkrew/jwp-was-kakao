package webserver.handler.response.header;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response302Header implements ResponseHeader {
    private static Response302Header response302Header = new Response302Header();
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Response302Header() {}

    public static Response302Header getInstance() { return response302Header; }

    @Override
    public void generate(DataOutputStream dataOutputStream, int lengthOfBodyContent, String accept) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 302 Found \r\n");
            dataOutputStream.writeBytes("Content-Type: " + accept + ";charset=utf-8 \r\n");
            dataOutputStream.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dataOutputStream.writeBytes("Location: /index.html \r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
