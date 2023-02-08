package web.controller;

import http.Body;
import http.ContentType;
import http.HttpCookies;
import http.HttpHeaders;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import utils.HandlebarsTemplateUtils;
import web.domain.MemoryUserRepository;
import web.infra.SessionManager;

import java.util.Objects;

import static http.HttpCookies.SESSION_ID;
import static http.HttpHeaders.*;

public class GetUserListController implements Controller {

    private static final String PATH = "/user/list";
    private static final String RESOURCE_PATH = "user/list";
    private static final String LOGIN_PAGE_PATH = "/login.html";

    private final SessionManager sessionManager;

    public GetUserListController(SessionManager sessionManager) {
        super();
        this.sessionManager = sessionManager;
    }

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        if (!verifyLogin(httpRequest)) {
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

    private boolean verifyLogin(HttpRequest httpRequest) {
        HttpCookies httpCookies = httpRequest.getCookies();
        String sessionId = httpCookies.get(SESSION_ID);

        return Objects.nonNull(sessionId) && sessionManager.getAttribute(sessionId)
                .isPresent();
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod()) && Objects.equals(PATH, httpRequest.getPath());
    }

}
