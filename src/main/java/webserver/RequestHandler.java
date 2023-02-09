package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestMethod;
import webserver.request.HttpRequestParser;
import webserver.request.QueryStringParser;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseContentType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

import static webserver.response.HttpResponseContentType.*;
import static webserver.response.HttpResponseHeader.*;
import static webserver.response.HttpResponseStatus.OK;
import static webserver.response.HttpResponseStatus.REDIRECT;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = HttpRequestParser.parse(in);
            HttpResponse response = HttpResponse.of(new DataOutputStream(out));

            if (request.getMethod() == HttpRequestMethod.GET) {
                doGet(request, response);
            } else if (request.getMethod() == HttpRequestMethod.POST) {
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
            Map<String, String> attributes = QueryStringParser.parseQueryString(query);

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
        if (isStaticFiles(request.getUri().getPath())) {
            byte[] body = FileIoUtils.loadFileFromClasspath("static" + request.getUri().getPath());
            response200(response, body, getStaticFileContentType(request.getUri().getPath()));
            return;
        }

        if (request.getUri().getPath().endsWith(".html")) {
            byte[] body = FileIoUtils.loadFileFromClasspath("templates" + request.getUri().getPath());
            response200(response, body, HttpResponseContentType.HTML);
            return;
        }

        if (request.getUri().getPath().equals("/")) {
            byte[] body = "Hello world".getBytes();
            response200(response, body, HttpResponseContentType.HTML);
            return;
        }

        if (request.getUri().getPath().equals("/user/create")) {
            String query = request.getUri().getQuery();
            Map<String, String> attributes = QueryStringParser.parseQueryString(query);

            User user = new User(
                    attributes.get("userId"),
                    attributes.get("password"),
                    attributes.get("name"),
                    attributes.get("email")
            );

            DataBase.addUser(user);

            response200(response, new byte[0], HttpResponseContentType.HTML);
            return;
        }
    }

    private static HttpResponseContentType getStaticFileContentType(String uri) {
        return Arrays.stream(HttpResponseContentType.values())
                .filter(type -> uri.endsWith(type.getSuffix()))
                .findFirst()
                .orElseThrow();
    }

    private static boolean isStaticFiles(String uri) {
        return uri.endsWith(CSS.getSuffix()) || uri.endsWith(JS.getSuffix()) ||
                uri.endsWith(WOFF.getSuffix()) || uri.endsWith(TTF.getSuffix()) ||
                uri.endsWith(ICO.getSuffix());
    }

    private void response302(HttpResponse response, String redirectUrl) {
        response.setStatusLine(REDIRECT);
        response.addHeader(LOCATION, redirectUrl);
    }

    private void response200(HttpResponse response, byte[] body, HttpResponseContentType contentType) {
        response.setStatusLine(OK);
        response.addHeader(CONTENT_TYPE, contentType.getContentType());
        response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
        response.addBody(body);
    }
}
