package webserver;

import db.DataBase;
import java.util.Objects;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import utils.FileIoUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private UserService userService;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.userService = new UserService();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            HttpRequest httpRequest = new HttpRequest(bufferedReader);
            handleHttpRequest(httpRequest, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            logger.error("No File");
        }
    }

    private void handleHttpRequest(HttpRequest httpRequest, OutputStream out)
            throws IOException, URISyntaxException, NullPointerException {
        DataOutputStream dos = new DataOutputStream(out);

        if (httpRequest.getMethod() == HttpMethod.POST) {
            handlePostMethodHttpRequest(httpRequest, dos);
        }
        if (httpRequest.getMethod() == HttpMethod.GET) {
            handleGetMethodHttpRequest(httpRequest, dos);
        }
    }

    private void handlePostMethodHttpRequest(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        String path = httpRequest.getPath();
        String requestBody = httpRequest.getBody();
        requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
        MultiValueMap<String, String> requestParams = UriComponentsBuilder.fromUriString(path)
                .query(requestBody)
                .build()
                .getQueryParams();

        if (path.equals("/user/create")) {
            userService.addUser(requestParams);
            HttpResponse httpResponse = new HttpResponse(HttpStatusCode.FOUND, null);
            httpResponse.addHeader("Location", "http://localhost:8080/index.html");
            httpResponse.response(dos);
        }
    }

    private void handleGetMethodHttpRequest(HttpRequest httpRequest, DataOutputStream dos)
            throws IOException {
        String path = httpRequest.getPath();
        byte[] body;
        String contentType;

        try {
            body = FileIoUtils.getBodyFromPath(path);
            contentType = Files.probeContentType(new File(path).toPath());
        } catch (Exception e) {
            body = "Hello world".getBytes();
            contentType = "text/html;charset=utf-8";
        }

        HttpResponse httpResponse = new HttpResponse(HttpStatusCode.OK, body);
        httpResponse.addHeader("Content-Type", contentType);
        httpResponse.addHeader("Content-Length", String.valueOf(body.length));
        httpResponse.response(dos);
    }
}
