package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
            HttpRequest request = HttpRequestParser.parse(in);
            DataOutputStream dos = new DataOutputStream(out);

            if(request.getMethod().equals("GET")) {
                doGet(request, dos);
            }

            if(request.getMethod().equals("POST")) {
                doPost(request, dos);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void doPost(HttpRequest request, DataOutputStream dos) {

        if(request.getUri().getPath().equals("/user/create")) {
            String query = request.getBody();
            String[] queryParameters  = query.split("&");
            Map<String, String> attributes = new HashMap<>();

            for (String queryParameter : queryParameters) {
                String key = queryParameter.split("=")[0];
                String value = queryParameter.split("=")[1];
                attributes.put(key, value);
            }

            User user = new User(
                    attributes.get("userId"),
                    attributes.get("password"),
                    attributes.get("name"),
                    attributes.get("email")
            );

            DataBase.addUser(user);

            response200HtmlHeader(dos, 0);
        }
    }

    private void doGet(HttpRequest request, DataOutputStream dos) throws IOException, URISyntaxException {
        byte[] body = null;
        if (request.getUri().getPath().endsWith(".css")) {
            body = FileIoUtils.loadFileFromClasspath("static" + request.getUri().getPath());
            response200CssHeader(dos, body.length);
            responseBody(dos, body);
        }

        if(request.getUri().getPath().endsWith(".html")) {
            body = FileIoUtils.loadFileFromClasspath("templates" + request.getUri().getPath());
            response200HtmlHeader(dos, body.length);
            responseBody(dos, body);
        }

        if(request.getUri().getPath().equals("/")) {
            body = "Hello world".getBytes();
            response200HtmlHeader(dos, body.length);
            responseBody(dos, body);
        }

        if(request.getUri().getPath().equals("/user/create")) {
            String query = request.getUri().getQuery();
            String[] queryParameters  = query.split("&");
            Map<String, String> attributes = new HashMap<>();

            for (String queryParameter : queryParameters) {
                String key = queryParameter.split("=")[0];
                String value = queryParameter.split("=")[1];
                attributes.put(key, value);
            }
            
            User user = new User(
                    attributes.get("userId"),
                    attributes.get("password"),
                    attributes.get("name"),
                    attributes.get("email")
            );

            DataBase.addUser(user);

            response200HtmlHeader(dos, 0);
        }

    }

    private void response200HtmlHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css; charset=utf-8 \r\n");
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
