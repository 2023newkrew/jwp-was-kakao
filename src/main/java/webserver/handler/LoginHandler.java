package webserver.handler;

import db.DataBase;
import java.util.Map;
import model.User;
import webserver.response.HttpResponse;
import webserver.request.HttpRequest;
import webserver.session.Session;

public class LoginHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        Map<String, String> body = request.getBodyLikeQueryParams();
        User user = DataBase.findUserById(body.get("userId"));
        if (verifyUser(user, body.get("password"))) {
            return getLoginFailResponse(request);
        }
        return getLoginSuccessResponse(request, user);
    }

    private static boolean verifyUser(User user, String password) {
        return user == null || !user.checkPassword(password);
    }

    private static HttpResponse getLoginSuccessResponse(HttpRequest request, User user) {
        Session session = request.getSession();
        session.setAttribute("user", user);
        HttpResponse response = HttpResponse.found(
                new byte[0],
                request.getFilenameExtension(),
                "/index.html"
        );
        response.setCookie("JSESSIONID", session.getId());
        return response;
    }

    private static HttpResponse getLoginFailResponse(HttpRequest request) {
        return HttpResponse.found(
                new byte[0],
                request.getFilenameExtension(),
                "/user/login_failed.html"
        );
    }

}
