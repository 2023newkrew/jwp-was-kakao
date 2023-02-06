package web;

import error.ApplicationException;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpRequestUtils;
import web.controller.Controller;
import web.controller.Controllers;

import java.io.*;
import java.net.Socket;

import static error.ErrorType.*;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Controllers controllers;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controllers = new Controllers();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            HttpRequest httpRequest = HttpRequestUtils.createHttpRequest(br);
            HttpResponse httpResponse = doService(httpRequest);

            doResponse(dos, httpResponse.toByte());
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ApplicationException(BUFFER_READ_FAILED, e.getMessage());
        }
    }

    private void doResponse(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ApplicationException(DATA_WRITE_FAILED, e.getMessage());
        }
    }

    private HttpResponse doService(HttpRequest httpRequest) {
        Controller controller = controllers.getController(httpRequest);
        return controller.run(httpRequest);
    }
}
