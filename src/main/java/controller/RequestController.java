package controller;

import db.DataBase;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import model.RequestInfo;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    public void createUser(RequestInfo request, DataOutputStream dos) throws URISyntaxException {
        User user = new User(
                request.getBodyValue("userId"),
                request.getBodyValue("password"),
                request.getBodyValue("name"),
                request.getBodyValue("email")
        );
        DataBase.addUser(user);
        response302Header(dos, new URI("http://localhost:8080/index.html"));
    }

    public void login(RequestInfo request, DataOutputStream dos) {

    }

    public void sendPage(RequestInfo request, DataOutputStream dos) {
        byte[] body = request.getResponse();
        response200Header(dos, body.length, request.getAccept());
        responseBody(dos, body);
    }

    private void setCookie(DataOutputStream dos, String name, String value) {
        try {
            dos.writeBytes("Set-Cookie: " + name + "=" + value + " \r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, URI uri) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + uri + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
