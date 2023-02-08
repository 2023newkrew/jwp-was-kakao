package webserver.handler;

import service.UserService;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.util.Objects;
import java.util.UUID;

public class LoginHandler extends AbstractHandler {

    @Override
    protected void doGet(final HttpRequest request, final HttpResponse response) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(final HttpRequest request, final HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        boolean isAuthenticated = new UserService().login(userId, password);
        if (isAuthenticated) {
            String jsessionid = request.getCookies().getCookie("JSESSIONID");
            /* jssesionid 세팅 */
            if (Objects.isNull(jsessionid)) {
                UUID uuid = UUID.randomUUID();
                response.setHeader("Set-Cookie", "JSESSIONID=" + uuid + "; Path=/");
            }
            response.setHeader("Location", "/index.html");
        } else {
            response.setHeader("Location", "/user/login_failed.html");
        }
        response.setStatus(HttpStatus.FOUND);
    }
}
