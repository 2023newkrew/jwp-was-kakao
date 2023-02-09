package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.StaticResourceController;

public class RequestHandler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    private static final StaticResourceController STATIC_RESOURCE_CONTROLLER = new StaticResourceController();

    private final Socket connection;
    private final List<Controller> controllers;

    public RequestHandler(Socket connectionSocket, List<Controller> controllers) {
        this.connection = connectionSocket;
        this.controllers = controllers;
    }

    public void run() {
        LOGGER.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = HttpRequest.from(in);
            HttpResponse response = handleRequest(request);
            String contentType = getContentType(request);
            response.setContentType(contentType);

            writeResponse(out, response);
        } catch (IOException | URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private HttpResponse handleRequest(HttpRequest request) throws IOException, URISyntaxException {
        Controller controller = controllers.stream()
                .filter(c -> c.isHandleable(request))
                .findAny()
                .orElse(STATIC_RESOURCE_CONTROLLER);

        return controller.handle(request);
    }

    private String getContentType(HttpRequest request) {
        String accept = request.getHeader("Accept");

        if (accept == null || accept.isBlank()) {
            return "text/plain";
        }
        return accept.split(",")[0] + ";charset=utf-8";
    }

    private void writeResponse(OutputStream out, HttpResponse response) {
        DataOutputStream dos = new DataOutputStream(out);
        responseHeader(dos, response);
        responseBody(dos, response.getBody());
    }

    private void responseHeader(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(response.getStatusLine() + " \r\n");

            for (var entry : response.getHeader().entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
