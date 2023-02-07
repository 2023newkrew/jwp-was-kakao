package webserver;

import controller.FrontController;
import http.CustomHttpRequest;
import http.CustomHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpUtils;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {
            CustomHttpRequest request = HttpUtils.readHttpRequest(br);
            DataOutputStream dos = new DataOutputStream(out);
            CustomHttpResponse response = FrontController.getInstance().getHttpResponse(request);
            HttpUtils.respondHttpResponse(dos, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
