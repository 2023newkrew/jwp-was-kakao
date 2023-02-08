package web.controller;

import http.HttpCookies;
import http.HttpHeaders;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import utils.ParameterUtils;
import utils.SessionIdGenerator;
import web.domain.MemoryUserRepository;
import web.domain.User;
import web.infra.SessionManager;

import java.util.Map;
import java.util.Objects;

import static http.HttpCookies.PATH;
import static http.HttpCookies.SESSION_ID;
import static http.HttpHeaders.LOCATION;
import static http.HttpHeaders.SET_COOKIE;

public class PostLoginController implements Controller {

    private static final String LOGIN_PATH = "/user/login";
    private static final String INDEX_PAGE_PATH = "/index.html";
    private static final String LOGIN_FAILED_PAGE_PATH = "/user/login_failed.html";

    private final SessionIdGenerator sessionIdGenerator;
    private final SessionManager sessionManager;

    public PostLoginController(SessionIdGenerator sessionIdGenerator, SessionManager sessionManager) {
        this.sessionIdGenerator = sessionIdGenerator;
        this.sessionManager = sessionManager;
    }

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        Map<String, String> params = ParameterUtils.parse(httpRequest.getBody());
        return MemoryUserRepository.findUserById(params.get("userId"))
                .filter(user -> user.checkPassword(params.get("password")))
                .map(this::createLoginSuccessResponse)
                .orElseGet(this::createLoginFailedResponse);
    }

    private HttpResponse createLoginSuccessResponse(User user) {
        String sessionId = sessionIdGenerator.generate();
        sessionManager.addAttribute(sessionId, user.getUserId());

        return HttpResponse.redirect(() -> {
            HttpCookies httpCookies = new HttpCookies();
            httpCookies.put(SESSION_ID, sessionId);
            httpCookies.put(PATH, "/");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(LOCATION, INDEX_PAGE_PATH);
            httpHeaders.put(SET_COOKIE, httpCookies.toString());

            return httpHeaders;
        });
    }

    private HttpResponse createLoginFailedResponse() {
        return HttpResponse.redirect(() -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(LOCATION, LOGIN_FAILED_PAGE_PATH);

            return httpHeaders;
        });
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.POST, httpRequest.getMethod()) && Objects.equals(LOGIN_PATH, httpRequest.getPath());
    }
}
