package webserver.handler.response.body;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseBody {
    private static ResponseBody responseBody = new ResponseBody();
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private ResponseBody() {}

    public static ResponseBody getInstance() { return responseBody; }

    public void generate(DataOutputStream dataOutputStream, byte[] body) {
        try {
            dataOutputStream.write(body, 0, body.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
