package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.FrontController;
import webserver.http.request.Request;
import webserver.http.response.Response;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            Request request = new Request(br);
            Response response = new Response(request);

            handleRequest(request, response);
            sendResponse(dos, response);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handleRequest(Request request, Response response) {
        FrontController.getMappedMethod(request)
                .handle(request, response);
    }

    private void sendResponse(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes(response.getHeader());
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}

