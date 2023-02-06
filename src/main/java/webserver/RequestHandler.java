package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestParser;
import webserver.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static webserver.response.HttpResponse.HttpResponseHeader.*;
import static webserver.response.HttpResponse.HttpResponseStatus.OK;
import static webserver.response.HttpResponse.HttpResponseStatus.REDIRECT;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = HttpRequestParser.parse(in);
            HttpResponse response = HttpResponse.of(new DataOutputStream(out));

            if (request.getMethod().equals("GET")) {
                doGet(request, response);
            } else if (request.getMethod().equals("POST")) {
                doPost(request, response);
            }

            response.send();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void doPost(HttpRequest request, HttpResponse response) {

        if (request.getUri().getPath().equals("/user/create")) {
            String query = request.getBody();
            String[] queryParameters = query.split("&");
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

            response302(response, "/index.html");
        }
    }

    private void doGet(HttpRequest request, HttpResponse response) throws IOException, URISyntaxException {
        byte[] body;
        if (request.getUri().getPath().endsWith(".css")) {
            body = FileIoUtils.loadFileFromClasspath("static" + request.getUri().getPath());
            response200Css(response, body);
        }

        if (request.getUri().getPath().endsWith(".html")) {
            body = FileIoUtils.loadFileFromClasspath("templates" + request.getUri().getPath());
            response200Html(response, body);
        }

        if (request.getUri().getPath().equals("/")) {
            body = "Hello world".getBytes();
            response200Html(response, body);
        }

        if (request.getUri().getPath().equals("/user/create")) {
            String query = request.getUri().getQuery();
            String[] queryParameters = query.split("&");
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

            response200Html(response, new byte[0]);
        }
    }

    private void response200Html(HttpResponse response, byte[] body) {
        response.setStatusLine(OK);
        response.addHeader(CONTENT_TYPE, "text/html; charset=utf-8");
        response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
        response.addBody(body);
    }

    private void response302(HttpResponse response, String redirectUrl) {
        response.setStatusLine(REDIRECT);
        response.addHeader(LOCATION, redirectUrl);
    }

    private void response200Css(HttpResponse response, byte[] body) {
        response.setStatusLine(OK);
        response.addHeader(CONTENT_TYPE, "text/css; charset=utf-8");
        response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
        response.addBody(body);
    }
}
