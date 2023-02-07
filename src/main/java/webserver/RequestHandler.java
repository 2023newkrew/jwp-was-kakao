package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Map;
import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream(); BufferedReader br = new BufferedReader(
                new InputStreamReader(in))) {
            RequestParser requestParser = new RequestParser(br);

            HttpRequest httpRequest = requestParser.buildHttpRequest();
            Map<String, String> userInfo = httpRequest.getBody();

            User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"),
                    userInfo.get("email"));
            DataBase.addUser(user);

            DataOutputStream dos = new DataOutputStream(out);

            // resolver
            byte[] body;
            if (httpRequest.isPOSTMethod()) {
                responseRedirectHome(dos);
                return;
            }
            try {
                if (httpRequest.getUrl().endsWith("html")) {
                    body = FileIoUtils.loadFileFromClasspath("./templates" + httpRequest.getUrl());
                } else {
                    body = FileIoUtils.loadFileFromClasspath("./static" + httpRequest.getUrl());
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }

            response200Header(dos, httpRequest.getContentType(), body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseRedirectHome(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Content-Type: text/html; charset=utf-8 \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "; charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
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
