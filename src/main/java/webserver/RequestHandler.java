package webserver;

import db.DataBase;
import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = HttpRequestUtils.createHttpRequest(br);
            DataOutputStream dos = new DataOutputStream(out);

            byte[] body;
            if (request.getUrl().equals("/")) {
                body = "Hello world".getBytes();
                response200Header(dos, body.length, request);
            } else if (request.getUrl().contains(".html")) {
                body = FileIoUtils.loadFileFromClasspath("./templates" + request.getUrl());
                response200Header(dos, body.length, request);
            } else if (request.getUrl().contains(".css")) {
                body = FileIoUtils.loadFileFromClasspath("./static" + request.getUrl());
                response200Header(dos, body.length, request);
            } else if (request.getUrl().startsWith("/user/create")) {
                body = "".getBytes();
                User user = new User(
                        request.getQuery().get("userId"),
                        request.getQuery().get("password"),
                        request.getQuery().get("name"),
                        request.getQuery().get("email")
                );
                DataBase.addUser(user);
                response302Header(dos, "/index.html");
            } else {
                body = "Hello world".getBytes();
                response200Header(dos, body.length, request);
            }

            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, HttpRequest request) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            String contentType = request.getHeaders().getOrDefault("Accept", "text/html").split(",")[0];
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Location: " + redirectUrl + " \r\n");
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
