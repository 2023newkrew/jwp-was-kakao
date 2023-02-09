package controller;

import org.springframework.util.AntPathMatcher;
import utils.FileIoUtils;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

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

    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException, URISyntaxException {
        if (httpRequest.getMethod().equals(HttpMethod.POST)) {
            PostMethodController postMethodController = controllerMap.get(httpRequest.getUrl());
            postMethodController.process(httpRequest, httpResponse);
            return;
        }

        String contentType = Optional.ofNullable(httpRequest.getHeader("Accept"))
                .map(str -> str.split(",")[0])
                .orElse("text/html;charset=utf-8");

        byte[] body = loadBody(httpRequest.getUrl());

        httpResponse.setContentType(contentType);
        httpResponse.setBody(body);
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
}
