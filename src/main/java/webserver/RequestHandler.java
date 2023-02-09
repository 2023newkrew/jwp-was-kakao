package webserver;

import common.HttpHeader;
import common.HttpRequest;
import common.HttpResponse;
import common.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.*;

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
            DispatcherServlet servlet = new DispatcherServlet();

            // 요청 처리 및 응답
            handleWithException(reader, dos, servlet);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handleWithException(BufferedReader reader, DataOutputStream dos, DispatcherServlet servlet) throws IOException {
       try {
           handle(reader, dos, servlet);
       } catch (CustomException e) {
           sendResponse(dos, new HttpResponse(e.getStatusCode(), e.getMessage()));
           logger.error("[" + e.getStatusCode() + "] " + e.getMessage());
       } catch (Exception e) {
           sendResponse(dos, new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR));
           logger.error("[" + HttpStatus.INTERNAL_SERVER_ERROR + "] " + e.getMessage());
       }
    }

    private void handle(BufferedReader reader, DataOutputStream dos, DispatcherServlet servlet) throws IOException {
        HttpRequest request = Parser.parseRequest(reader);
        HttpResponse response = new HttpResponse();

        servlet.dispatch(request, response);

        sendResponse(dos, response);
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
