package webserver;

import common.HttpHeader;
import common.HttpRequest;
import common.HttpResponse;
import common.HttpStatus;
import controller.Controller;
import controller.FileController;
import controller.RootController;
import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import support.IllegalMethodException;
import support.MethodNotAllowedException;
import support.PathNotFoundException;
import support.UnsupportedContentTypeException;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

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
            DataOutputStream dos = new DataOutputStream(out);

            // 요청 처리 및 응답
            handleWithException(reader, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handleWithException(BufferedReader reader, DataOutputStream dos) throws IOException {
       try {
           handle(reader, dos);
       } catch (IllegalMethodException e) {
           sendResponse(dos, new HttpResponse(HttpStatus.BAD_REQUEST));
       } catch (PathNotFoundException e) {
           sendResponse(dos, new HttpResponse(HttpStatus.NOT_FOUND));
       } catch (MethodNotAllowedException e) {
           sendResponse(dos, new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED));
       } catch (UnsupportedContentTypeException e) {
           sendResponse(dos, new HttpResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
       } catch (Exception e) {
           sendResponse(dos, new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR));
       }
    }

    private void handle(BufferedReader reader, DataOutputStream dos) throws IOException {
        HttpRequest request = Parser.parseRequest(reader);
        Controller controller = chooseHandler(request);

        HttpResponse response = new HttpResponse();
        controller.handleRequest(request, response);
        sendResponse(dos, response);
    }

    private Controller chooseHandler(HttpRequest request) {
        String uri = request.getUri();
        if (uri.endsWith(".html") || uri.endsWith(".css")) {
            return new FileController();
        }
        else if (uri.startsWith("/user")) {
            return new UserController(new UserService());
        }
        else if (uri.equals("/")) {
            return new RootController();
        }
        else {
            throw new PathNotFoundException();
        }
    }

    private void sendResponse(DataOutputStream dos, final HttpResponse response) {
        if (response.getHttpStatus().equals(HttpStatus.OK)) {
            responseHeaderWithContent(dos, response);
        }
        else if (response.getHttpStatus().equals(HttpStatus.FOUND)) {
            responseHeaderWithLocation(dos, response);
        }
        else { // 400, 404, 405, 415, 500
            responseHeader(dos, response);
        }
        responseBody(dos, response.getBody());
    }

    private void responseHeaderWithContent(DataOutputStream dos, final HttpResponse response) {
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
        HttpStatus httpStatus = Optional.ofNullable(response.getHttpStatus()).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
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
