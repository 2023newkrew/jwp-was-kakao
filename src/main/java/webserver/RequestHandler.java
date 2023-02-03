package webserver;

import db.DataBase;
import enums.ContentType;
import exceptions.InvalidQueryParameterException;
import exceptions.ResourceNotFoundException;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Map;

import static utils.IOUtils.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }


    public void run() {

        System.out.println("connection");
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        DataOutputStream dos;
        InputStream in;
        OutputStream out;
        try {
            in = connection.getInputStream();
            out = connection.getOutputStream();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }
        try {
            String path = extractPath(extractRequestFirstLine(in));
            byte[] body;

            if (path.startsWith("/user/create")) {
                Map<String, String> userInfo = IOUtils.extractUser(path);
                if (userInfo.isEmpty()) {
                }
                User user = new User(userInfo.get("userId"), userInfo.get("password"), URLDecoder.decode(userInfo.get("name")), userInfo.get("email"));
                DataBase.addUser(user);
            }
            if (path.equals("/")) {
                body = "Hello world".getBytes();
                dos = new DataOutputStream(out);
                response200Header(dos, body.length, ContentType.HTML);
                responseBody(dos, body);
                return;
            }

            ContentType contentType = ContentType.fromFilename(path);

            String resourcePath = FileIoUtils.getResourcePath(path, contentType);
            body = FileIoUtils.loadFileFromClasspath(resourcePath);

            dos = new DataOutputStream(out);
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } catch (ResourceNotFoundException e) {
            dos = new DataOutputStream(out);
            response404NotFoundHeader(dos);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InvalidQueryParameterException e) {
            dos = new DataOutputStream(out);
            response400BadRequestHeader(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404NotFoundHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 NOT FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response400BadRequestHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 400 BAD REQUEST \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, ContentType contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType.getValue() + ";charset=utf-8 \r\n");
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
