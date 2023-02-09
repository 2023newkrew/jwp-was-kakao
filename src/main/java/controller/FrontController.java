package controller;

import org.springframework.util.AntPathMatcher;
import utils.FileIoUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class FrontController {
    private static final Map<String, Controller> controllerMap = Map.of(
            "POST /user/create", new UserSaveController(),
            "POST /user/login", new LoginController(),
            "GET /user/list", new UserListController(),
            "GET /user/login", new LoginPageController()
    );

    private static final String ROOT_PATH = "/";
    private static final String[] STATIC_PATH_PATTERNS = {"/css/*", "/fonts/*", "/images/*", "/js/*"};
    private static final String STATIC = "./static";
    private static final String TEMPLATES = "./templates";

    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws NotFoundException, RedirectException {
        try {
            String contentType = Optional.ofNullable(httpRequest.getHeader("Accept"))
                    .map(str -> str.split(",")[0])
                    .orElse("text/html;charset=utf-8");
            httpResponse.addHeader("Content-Type", contentType);

            String requestSignature = httpRequest.getMethod().name() + " " + httpRequest.getUrl();
            if (controllerMap.containsKey(requestSignature)) {
                Controller controller = controllerMap.get(requestSignature);
                controller.process(httpRequest, httpResponse);
                return;
            }

            byte[] body = loadBody(httpRequest.getUrl());
            httpResponse.changeBody(body);
        } catch (URISyntaxException e) {
            throw new NotFoundException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
