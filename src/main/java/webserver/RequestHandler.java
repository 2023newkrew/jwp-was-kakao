package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;
import utils.IOUtils;
import webserver.request.Request;
import webserver.response.Response;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            Request request = new Request(br);
            Response response = new Response(request);

            if(request.getHttpMethod().equals(HttpMethod.GET) && request.getPath().equals("/")) {
                response.setBody("Hello World!".getBytes());
                response.setStatus(HttpStatus.OK);
            } else if (request.getHttpMethod().equals(HttpMethod.POST) && request.getPath().equals("/user/create")) {
                User user = new User(
                        request.getBodyValue("userId"),
                        request.getBodyValue("password"),
                        request.getBodyValue("name"),
                        request.getBodyValue("email")
                );
                DataBase.addUser(user);
                response.setStatus(HttpStatus.FOUND);
                response.setLocation("http://localhost:8080/index.html");
            } else {
                // resource 응답
                String rootPath = "./templates";
                if (request.hasStaticPath()) {
                    rootPath = "./static";
                }
                setResource(rootPath + request.getPath(), response);
            }

            sendResponse(dos, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setResource(String path, Response response) {
        try {
            response.setBody(FileIoUtils.loadFileFromClasspath(path));
            response.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.setBody("404 Not Found - 요청한 페이지를 찾을 수 없습니다.".getBytes());
            response.setStatus(HttpStatus.NOT_FOUND);
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes(response.getHeader());
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

