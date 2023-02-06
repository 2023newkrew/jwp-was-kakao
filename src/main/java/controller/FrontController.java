package controller;

import org.springframework.util.AntPathMatcher;
import utils.FileIoUtils;
import webserver.HttpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class FrontController {
    private static final Map<String, PostMethodController> controllerMap = Map.of(
            "/user/create", new UserSaveController()
    );

    private static final String ROOT_PATH = "/";
    private static final String[] STATIC_PATH_PATTERNS = {"/css/*", "/fonts/*", "/images/*", "/js/*"};
    private static final String STATIC = "./static";
    private static final String TEMPLATES = "./templates";

    public void service(BufferedReader bufferedReader, DataOutputStream dos) throws IOException, URISyntaxException {
        HttpRequest httpRequest = new HttpRequest(bufferedReader);

        if (httpRequest.getMethod().equals("POST")) {
            PostMethodController postMethodController = controllerMap.get(httpRequest.getUrl());
            String location = postMethodController.process(httpRequest);
            response302(dos, location);
            return;
        }

        String contentType = Optional.ofNullable(httpRequest.getHeader("Accept"))
                .map(str -> str.split(",")[0])
                .orElse("text/html;charset=utf-8");

        byte[] body = loadBody(httpRequest.getUrl());

        response200(dos, contentType, body);
    }

    private byte[] loadBody(String requestUrl) throws IOException, URISyntaxException {
        if (requestUrl.equals(ROOT_PATH)) {
            return "Hello world".getBytes();
        }

        String pathPrefix = isStaticPath(requestUrl) ? STATIC : TEMPLATES;
        return FileIoUtils.loadFileFromClasspath(pathPrefix + requestUrl);
    }

    private boolean isStaticPath(String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return Arrays.stream(STATIC_PATH_PATTERNS)
                .anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private void response200(DataOutputStream dos, String contentType, byte[] body) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType + " \r\n");
        dos.writeBytes("Content-Length: " + body.length + " \r\n");
        dos.writeBytes("\r\n");
        dos.write(body);
        dos.flush();
    }

    private void response302(DataOutputStream dos, String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + location + " \r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }
}
