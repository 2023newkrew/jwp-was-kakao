package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

import utils.FileIoUtils;
import model.User;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            String request = new BufferedReader(new InputStreamReader(in)).readLine();
            String requestPath = request.split(" ")[1];
            byte[] body = new byte[0];
            String contentType = "";
            if (requestPath.endsWith(".html")) {
                body = FileIoUtils.loadFileFromClasspath("templates" + requestPath);
                contentType = "text/html;charset=utf-8";
            }
            else if (requestPath.startsWith("/css")) {
                body = FileIoUtils.loadFileFromClasspath("static" + requestPath);
                contentType = "text/css";
            }
            else if (requestPath.startsWith("/js")) {
                body = FileIoUtils.loadFileFromClasspath("static" + requestPath);
                contentType = "application/javascript;charset=utf-8";
            }
            else if (requestPath.startsWith("/images")) {
                body = FileIoUtils.loadFileFromClasspath("static" + requestPath);
                contentType = "images/*";
            }
            else if (requestPath.startsWith("/fonts")) {
                body = FileIoUtils.loadFileFromClasspath("static" + requestPath);
                contentType = "font/*";
            }
            else if (requestPath.startsWith("/user/create")) {
                String[] tokensPath = requestPath.split("\\?")[1].split("&");
                User user = new User(tokensPath[0].split("=")[1], tokensPath[1].split("=")[1], tokensPath[2].split("=")[1], tokensPath[3].split("=")[1]);
                DataBase.addUser(user);
                body = "Temp_Message: successfully Signed-up".getBytes();
            }
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } catch (IOException|URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + " \r\n");
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
