package web.controller;

import http.Body;
import http.ContentType;
import http.HttpHeaders;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import utils.IOUtils;
import web.validator.LoginValidator;

import java.util.Objects;

import static http.HttpHeaders.*;

public class GetResourceController implements Controller {

    private static final String INDEX_PAGE_PATH = "/index.html";
    private static final String LOGIN_PAGE_PATH = "/user/login.html";
    private static final String HTML_SUFFIX = ".html";
    private static final String ICON_SUFFIX = ".ico";
    private static final String TEMPLATE_PATH = "templates";
    private static final String STATIC_PATH = "static";

    private final LoginValidator loginValidator;

    public GetResourceController(LoginValidator loginValidator) {
        this.loginValidator = loginValidator;
    }

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (isLoginPageRequestWithLoginStatus(httpRequest)) {
            return createFailedResponse();
        }

        return createSuccessResponse(getSuffix(httpRequest) + httpRequest.getPath());
    }

    private HttpResponse createSuccessResponse(String resourcePath) {
        return HttpResponse.ok(
                () -> new Body(IOUtils.readFileFromClasspath(resourcePath)),
                body -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.put(CONTENT_TYPE, ContentType.from(resourcePath).toString());
                    httpHeaders.put(CONTENT_LENGTH, String.valueOf(body.length()));

                    return httpHeaders;
                });
    }

    private HttpResponse createFailedResponse() {
        return HttpResponse.redirect(() -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(LOCATION, INDEX_PAGE_PATH);

            return httpHeaders;
        });
    }

    private boolean isLoginPageRequestWithLoginStatus(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod())
                && Objects.equals(LOGIN_PAGE_PATH, httpRequest.getPath())
                && loginValidator.validate(httpRequest);
    }

    private String getSuffix(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (path.contains(HTML_SUFFIX) || path.contains(ICON_SUFFIX)) {
            return TEMPLATE_PATH;
        }

        return STATIC_PATH;
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod());
    }

}
