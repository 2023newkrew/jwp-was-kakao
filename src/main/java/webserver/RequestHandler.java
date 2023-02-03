package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(final Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
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
        if (request.getUri().endsWith(".html") | request.getUri().endsWith(".css")) {
            response.setBody(Parser.getFileContent(request.getUri()));
            response.setHttpStatus(HttpStatus.OK);
        }
        else if (request.getUri().startsWith("/user")) {
            Map<String, String> map = Parser.getUriParameters(request.getUri());
            new UserService().addUser(
                    map.get("userId"),
                    map.get("password"),
                    map.get("name"),
                    map.get("email")
            );
            response.setHeaders(new HashMap<>(){{
                put("Location", "/index.html");
            }});
            response.setHttpStatus(HttpStatus.FOUND);
        }
        else {
            response.setBody("Hello world".getBytes());
            response.setHttpStatus(HttpStatus.OK);
        }
    }

    private void doPost(final HttpRequest request, HttpResponse response) {
         if (request.getUri().startsWith("/user")) {
            Map<String, String> map = request.getParameter();

            new UserService().addUser(
                    map.get("userId"),
                    map.get("password"),
                    map.get("name"),
                    map.get("email")
            );

            response.setHeaders(new HashMap<>(){{
                put("Location", "/index.html");
            }});
            response.setHttpStatus(HttpStatus.FOUND);
        }
    }

    private void sendResponse(HttpResponse response, DataOutputStream dos) {
        switch (response.getHttpStatus()) {
            case OK:
                response200Header(dos, response.getBody().length);
                break;
            case FOUND:
                response302Header(dos, response.getHeaders().get("Location"));
        }
        responseBody(dos, response.getBody());
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: "+location+" \r\n");
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
            System.out.println(e.getMessage());
        }
    }
}
