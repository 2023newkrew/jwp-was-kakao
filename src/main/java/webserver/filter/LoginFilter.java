package webserver.filter;

import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.cookie.HttpCookie;
import webserver.http.session.SessionManager;

import java.util.Objects;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(final HttpRequest request, final HttpResponse response, final FilterChain chain) {
        System.out.println("Login Filter 호출 ///");
        /* 이미 로그인 되어 있으면 리다이렉트 */
        if (request.getPath().equals("/user/login.html")) {
            HttpCookie cookie = request.getCookie("JSESSIONID");
            if (Objects.nonNull(cookie)) {
                User user = (User) SessionManager.findSession(cookie.getValue()).getAttribute("user");
                if (Objects.nonNull(user)) {
                    response.setHeader("Location", "/index.html");
                    response.setStatus(HttpStatus.FOUND);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
