package webserver;

import controller.ResourceController;
import controller.UserController;
import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ResourceController resourceController;
    private final UserController userController;

    public RequestHandler(Socket connectionSocket, ResourceController resourceController, UserController userController) {
        this.connection = connectionSocket;
        this.resourceController = resourceController;
        this.userController = userController;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = HttpRequestParser.parse(in);
            DataOutputStream dos = new DataOutputStream(out);

            if(request.getMethod().equals("GET")) {
                doGet(request, dos);
            }

            if(request.getMethod().equals("POST")) {
                doPost(request, dos);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void doPost(HttpRequest request, DataOutputStream dos) throws IOException {

        if(request.getUri().getPath().equals("/user/create")) {
            response(dos, userController.createUserPost(request).getBytes());
        }
    }

    private void doGet(HttpRequest request, DataOutputStream dos) throws IOException, URISyntaxException {
        if(request.getUri().getPath().equals("/")) {
            byte[] body = "Hello world".getBytes();
            HttpResponse response = new HttpResponse.Builder()
                    .addAttribute(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8")
                    .body(body)
                    .build();
            response(dos, response.getBytes());
        }

        if (request.checkStaticResource()) {
            response(dos, resourceController.staticResource(request).getBytes());
        }

        if(request.checkHtmlResource()) {
            response(dos, resourceController.templateResource(request).getBytes());
        }

        if(request.getUri().getPath().equals("/user/create")) {
            response(dos, userController.createUserGet(request).getBytes());
        }

    }

    private void response(DataOutputStream dos, byte[] data) {
        try {
            dos.write(data, 0, data.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
