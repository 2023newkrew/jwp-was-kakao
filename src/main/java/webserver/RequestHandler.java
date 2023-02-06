package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String DEFAULT_RESPONSE = "Hello world";
    private static final String REDIRECT_PATH = "/index.html";

    private final Socket connection;

    public RequestHandler(final Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            // 요청 및 응답
            HttpRequest request = Parser.parseRequestMessage(reader);
            HttpResponse response = new HttpResponse();
            switch (request.getMethod()) {
                case GET:
                    doGet(request, response);
                    break;
                case POST:
                    doPost(request, response);
            }
            sendResponse(response, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void doGet(final HttpRequest request, HttpResponse response) {
        String uri = request.getUri();
        if (uri.endsWith(".html") | uri.endsWith(".css")) {
            response.setBody(Parser.getFileContent(uri));
            response.setHttpStatus(HttpStatus.OK);
        }
        else if (uri.startsWith("/user")) {
            Map<String, String> map = Parser.getUriParameters(request.getUri());
            new UserService().addUser(
                    map.get("userId"),
                    map.get("password"),
                    map.get("name"),
                    map.get("email")
            );
            response.setHeader(HttpHeader.LOCATION, REDIRECT_PATH);
            response.setHttpStatus(HttpStatus.FOUND);
        }
        else {
            response.setBody(DEFAULT_RESPONSE.getBytes());
            response.setHttpStatus(HttpStatus.OK);
        }
        // TODO : NOT FOUND
    }

    private void doPost(final HttpRequest request, HttpResponse response) {
         if (request.getUri().startsWith("/user")) {
            Map<String, String> map = request.getParameter();

            // TODO : BAD REQUEST
            new UserService().addUser(
                    map.get("userId"),
                    map.get("password"),
                    map.get("name"),
                    map.get("email")
            );

            response.setHeader(HttpHeader.LOCATION, REDIRECT_PATH);
            response.setHttpStatus(HttpStatus.FOUND);
        }
        // TODO : NOT FOUND
    }

    private void sendResponse(HttpResponse response, DataOutputStream dos) {
        switch (response.getHttpStatus()) {
            case OK:
                response200Header(dos, response);
                break;
            case FOUND:
                response302Header(dos, response);
        }
        responseBody(dos, response.getBody());
    }

    private void response200Header(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + response.getHeader(HttpHeader.CONTENT_LENGTH) + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + response.getHeader(HttpHeader.LOCATION) + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
