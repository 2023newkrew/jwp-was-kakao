package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class HttpResponse {
    private final static Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    public static final String TEMPLATE_PATH = "./templates/";

    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void forward(String path) {
        byte[] body = FileIoUtils.loadFileFromClasspath(TEMPLATE_PATH + path);
        logger.info("Forward to file : {}", path);
        response200Header(body.length);
        responseBody(body);
    }

    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
