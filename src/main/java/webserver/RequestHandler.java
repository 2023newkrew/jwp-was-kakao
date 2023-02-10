package webserver;

import http.request.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final FrontController frontController = new FrontController();
    private final HttpResponse response = new HttpResponse();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.info("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            handleRequest(bufferedReader);
            sendResponse(out);
            bufferedReader.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handleRequest(BufferedReader bufferedReader) {
        HttpRequest request = new HttpRequest(bufferedReader);
        frontController.service(request, response);
    }

    private void sendResponse(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        ResponseSender responseSender = new ResponseSender(dos, response);
        responseSender.forward();
        try {
            dos.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
