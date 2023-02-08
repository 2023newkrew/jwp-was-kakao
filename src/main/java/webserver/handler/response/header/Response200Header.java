package webserver.handler.response.header;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response200Header implements ResponseHeader {
    private static Response200Header response200Header = new Response200Header();
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Response200Header() {}

    public static Response200Header getInstance() { return response200Header; }

    @Override
    public void generate(DataOutputStream dataOutputStream, int lengthOfBodyContent, String accept) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");
            dataOutputStream.writeBytes("Content-Type: " + accept + ";charset=utf-8 \r\n");
            dataOutputStream.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
