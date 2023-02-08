package web.controller;

import http.Body;
import http.ContentType;
import http.HttpHeaders;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import utils.HandlebarsTemplateUtils;
import web.validator.LoginValidator;
import web.domain.MemoryUserRepository;

import java.util.Objects;

import static http.HttpHeaders.*;

public class GetUserListController implements Controller {

    private static final String PATH = "/user/list";
    private static final String RESOURCE_PATH = "user/list";
    private static final String LOGIN_PAGE_PATH = "/login.html";

    private final LoginValidator loginValidator;

    public GetUserListController(LoginValidator loginValidator) {
        this.loginValidator = loginValidator;
    }

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (!loginValidator.validate(httpRequest)) {
            return createFailedResponse();
        }

        return createSuccessResponse();
    }

    private HttpResponse createSuccessResponse() {
        return HttpResponse.ok(
                () -> new Body(HandlebarsTemplateUtils.create(RESOURCE_PATH, MemoryUserRepository.findAll())),
                body -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.put(CONTENT_TYPE, ContentType.TEXT_HTML.toString());
                    httpHeaders.put(CONTENT_LENGTH, String.valueOf(body.length()));

                    return httpHeaders;
                }
            );
    }

    private HttpResponse createFailedResponse() {
        return HttpResponse.redirect(() -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(LOCATION, LOGIN_PAGE_PATH);

            return httpHeaders;
        });
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod()) && Objects.equals(PATH, httpRequest.getPath());
    }

}
