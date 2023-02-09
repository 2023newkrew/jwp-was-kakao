package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.FrontController;
import webserver.http.SessionManager;
import webserver.http.request.Request;
import webserver.http.response.Response;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

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

            setSessionId(request, response);

            handleRequest(request, response);
            sendResponse(dos, response);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setSessionId(Request request, Response response) {
        if(!request.hasSessionId()) {
            response.getCookies().setCookie(SessionManager.SESSION_ID_NAME, UUID.randomUUID().toString());
            response.getCookies().setCookie("Path", "/");
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

