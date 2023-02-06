package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import support.IllegalMethodException;

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

            // 경로 매칭 / 요청 및 응답
            process(reader, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void process(BufferedReader reader, DataOutputStream dos) throws IOException {
        HttpRequest request;
        HttpResponse response = new HttpResponse();
        try {
            request = Parser.parseRequestMessage(reader);
        } catch (IllegalMethodException e) {
            sendResponse(dos, new HttpResponse(HttpStatus.BAD_REQUEST));
            return;
        } catch (Exception e) {
            sendResponse(dos, new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR));
            return;
        }

        String uri = request.getUri();
        if (uri.endsWith(".html") || uri.endsWith(".css")) {
            processFileRequest(request, response);
        }
        else if (uri.startsWith("/user")) {
            processUserRequest(request, response);
        }
        else if (uri.equals("/")) {
            processRootRequest(request, response);
        }
        else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        }
        sendResponse(dos, response);
    }

    private void processFileRequest(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            response.setBody(Parser.getFileContent(request.getUri()));
            response.setHttpStatus(HttpStatus.OK);
        }
        else {
            response.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private void processUserRequest(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)
                || request.getMethod().equals(HttpMethod.POST)) {
            Map<String, String> map = request.getParameter();
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
            response.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private void processRootRequest(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            response.setBody(DEFAULT_RESPONSE.getBytes());
            response.setHttpStatus(HttpStatus.OK);
        }
        else {
            response.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private void sendResponse(DataOutputStream dos, final HttpResponse response) {
        switch (response.getHttpStatus()) {
            case OK:
                responseHeaderWithContent(dos, response);
                break;
            case FOUND:
                responseHeaderWithLocation(dos, response);
                break;
            case BAD_REQUEST:
            case NOT_FOUND:
            case METHOD_NOT_ALLOWED:
                responseHeader(dos, response);
                break;
            default:
                responseHeader(dos, new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        responseBody(dos, response.getBody());
    }

    private void responseHeaderWithContent(DataOutputStream dos, HttpResponse response) {
        HttpStatus httpStatus = response.getHttpStatus();
        try {
            dos.writeBytes("HTTP/1.1 " + httpStatus.code + " " + httpStatus.message + " \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + response.getHeader(HttpHeader.CONTENT_LENGTH) + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseHeaderWithLocation(DataOutputStream dos, final HttpResponse response) {
        HttpStatus httpStatus = response.getHttpStatus();
        try {
            dos.writeBytes("HTTP/1.1 " + httpStatus.code + " " + httpStatus.message + " \r\n");
            dos.writeBytes("Location: " + response.getHeader(HttpHeader.LOCATION) + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseHeader(DataOutputStream dos, final HttpResponse response) {
        HttpStatus httpStatus = response.getHttpStatus();
        try {
            dos.writeBytes("HTTP/1.1 " + httpStatus.code + " " + httpStatus.message + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, final byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
