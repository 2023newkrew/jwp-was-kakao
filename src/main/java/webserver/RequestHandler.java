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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.User;
import model.request.HttpRequestFactory;
import model.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))
        ) {
            HttpRequest httpRequest = HttpRequestFactory.parse(br);
            HttpResponse httpResponse = getResponse(httpRequest);
            writeResponse(dos, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse getResponse(HttpRequest httpRequest) throws IOException, URISyntaxException {
        String contentType = getContentType(httpRequest);

        if (httpRequest.getPath().endsWith(".html")) {
            byte[] body = FileIoUtils.loadFileFromClasspath("templates" + httpRequest.getPath());
            return HttpResponse.ok(Map.of("Content-Type", contentType), body);
        }

        if (httpRequest.getPath().equals("/")) {
            return HttpResponse.ok(Map.of("Content-Type", contentType), "Hello world".getBytes());
        }

        if (httpRequest.getPath().equals("/query")) {
            return HttpResponse.ok(Map.of("Content-Type", contentType),
                    ("hello " + httpRequest.getParameter("name")).getBytes()
            );
        }

        if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getPath().equals("/user/create")) {
            Map<String, String> body = httpRequest.getBody();

            User user = new User(
                    body.get("userId"),
                    body.get("password"),
                    body.get("name"),
                    body.get("email"));
            DataBase.addUser(user);

            return HttpResponse.redirect(Map.of(), "/index.html");

        }

        if (httpRequest.getMethod().equals(HttpMethod.POST) && httpRequest.getPath().equals("/post")) {
            String bodyString = String.format("hello %s", httpRequest.getBody().get("name"));
            return HttpResponse.ok(Map.of("Content-Type", contentType), bodyString.getBytes());
        }

        return HttpResponse.ok(
                Map.of("Content-Type", contentType),
                FileIoUtils.loadFileFromClasspath("static" + httpRequest.getPath())
        );
    }



    private void writeResponse(DataOutputStream dos, HttpResponse httpResponse) {
        responseHeader(dos, httpResponse);
        responseBody(dos, httpResponse.getBody());
    }

    private String getContentType(HttpRequest httpRequest) throws IOException, URISyntaxException {
        String acceptValue = httpRequest.getHeaders().get("Accept");

        if (acceptValue == null || acceptValue.isBlank()) {
            return "text/html;charset=utf-8";
        }
        return acceptValue.split(",")[0];
    }

    private void responseHeader(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(response.getStatusLine() + " \r\n");
            for (var entry : response.getHeader().entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
            }
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