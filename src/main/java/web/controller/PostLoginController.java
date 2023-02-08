package web.controller;

import http.HttpCookie;
import http.HttpHeaders;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import utils.ParameterUtils;
import utils.SessionIdGenerator;
import web.domain.MemoryUserRepository;
import web.domain.User;

import java.util.Map;
import java.util.Objects;

import static http.HttpCookie.PATH;
import static http.HttpCookie.SESSION_ID;
import static http.HttpHeaders.LOCATION;
import static http.HttpHeaders.SET_COOKIE;

public class PostLoginController implements Controller {

    private static final String LOGIN_PATH = "/user/login";
    private static final String INDEX_PAGE_PATH = "/index.html";
    private static final String LOGIN_FAILED_PAGE = "/user/login_failed.html";

    private final SessionIdGenerator sessionIdGenerator;

    public PostLoginController(SessionIdGenerator sessionIdGenerator) {
        this.sessionIdGenerator = sessionIdGenerator;
    }

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        Map<String, String> params = ParameterUtils.parse(httpRequest.getBody());
        return MemoryUserRepository.findUserById(params.get("userId"))
                .filter(user -> user.checkPassword(params.get("password")))
                .map(user -> createLoginSuccessResponse())
                .orElseGet(this::createLoginFailedResponse);
    }

    private HttpResponse createLoginSuccessResponse() {
        return HttpResponse.redirect(() -> {
            HttpCookie httpCookie = new HttpCookie();
            httpCookie.put(SESSION_ID, sessionIdGenerator.generate());
            httpCookie.put(PATH, "/");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(LOCATION, INDEX_PAGE_PATH);
            httpHeaders.put(SET_COOKIE, httpCookie.toString());

            return httpHeaders;
        });
    }

    private HttpResponse createLoginFailedResponse() {
        return HttpResponse.redirect(() -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(LOCATION, LOGIN_FAILED_PAGE);

            return httpHeaders;
        });
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.POST, httpRequest.getMethod()) && Objects.equals(LOGIN_PATH, httpRequest.getPath());
    }
}
