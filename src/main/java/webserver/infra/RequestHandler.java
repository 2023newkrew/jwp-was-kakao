package webserver.infra;

import model.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ResponseUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static utils.RequestBuilder.*;
import static utils.ResponseUtils.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            doResponse(dos, FrontController.handleRequest(getHttpRequest(bufferedReader)));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
