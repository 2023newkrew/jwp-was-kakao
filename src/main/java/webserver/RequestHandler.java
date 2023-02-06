package webserver;

import http.request.HttpMethod;
import http.request.HttpRequest;
import http.request.RequestLine;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.info("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            HttpRequest httpRequest = new HttpRequest(bufferedReader);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = new HttpResponse(dos);

            handle(httpRequest, httpResponse);

            bufferedReader.close();
            dos.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handle(HttpRequest request, HttpResponse response) {
        RequestLine requestLine = request.getRequestLine();
        HttpMethod method = requestLine.getHttpMethod();
        String path = requestLine.getRequestUri().getPath();

        Controller controller = Router.getController(method, path);

        if (controller == null) {
            response.forward(path);
            return;
        }

        controller.handle(request, response);
    }
}
