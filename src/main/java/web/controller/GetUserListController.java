package web.controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import error.ApplicationException;
import http.Body;
import http.ContentType;
import http.HttpCookies;
import http.HttpHeaders;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import web.domain.MemoryUserRepository;
import web.domain.User;
import web.infra.SessionManager;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import static error.ErrorType.FILE_READ_FAILED;
import static http.HttpCookies.SESSION_ID;
import static http.HttpHeaders.*;

public class GetUserListController implements Controller {

    private static final String PATH = "/user/list";
    private static final String RESOURCE_PREFIX = "/templates";
    private static final String RESOURCE_SUFFIX = ".html";
    private static final String RESOURCE_PATH = "user/profile";
    private static final String INDEX_PAGE_PATH = "/index.html";

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
                this::createUserListPage,
                body -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.put(CONTENT_TYPE, ContentType.from(RESOURCE_SUFFIX).toString());
                    httpHeaders.put(CONTENT_LENGTH, String.valueOf(body.length()));

                    return httpHeaders;
                }
            );
    }

    private Body createUserListPage() {
        try {
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix(RESOURCE_PREFIX);
            loader.setSuffix(RESOURCE_SUFFIX);
            Handlebars handlebars = new Handlebars(loader);

            Template template = handlebars.compile(RESOURCE_PATH);

            Collection<User> users = MemoryUserRepository.findAll();
            return new Body(template.apply(users));
        } catch (IOException e) {
            throw new ApplicationException(FILE_READ_FAILED, e.getMessage());
        }
    }

    private HttpResponse createFailedResponse() {
        return HttpResponse.redirect(() -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(LOCATION, INDEX_PAGE_PATH);

            return httpHeaders;
        });
    }

    private boolean verifyLogin(HttpRequest httpRequest) {
        HttpCookies httpCookies = httpRequest.getCookies();
        String sessionId = httpCookies.get(SESSION_ID);

        return sessionManager.getAttribute(sessionId)
                .isPresent();
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod()) && Objects.equals(PATH, httpRequest.getPath());
    }

}
