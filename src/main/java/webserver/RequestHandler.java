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

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.info("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(bufferedReader);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = new HttpResponse();
            frontController.service(request, response);

            ResponseSender responseSender = new ResponseSender(dos, response);
            responseSender.forward();

            bufferedReader.close();
            dos.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
