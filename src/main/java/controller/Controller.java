package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Controller {

    static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public abstract void process(HttpRequest request, DataOutputStream dos) throws IOException;

    void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
