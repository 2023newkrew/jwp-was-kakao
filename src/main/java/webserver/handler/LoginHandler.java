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
            return HttpResponse.found("/user/login_failed.html");
        }
        return getLoginSuccessResponse(request, user);
    }

    private static boolean verifyUser(User user, String password) {
        return user == null || !user.checkPassword(password);
    }

    private static HttpResponse getLoginSuccessResponse(HttpRequest request, User user) {
        Session session = request.getOrCreateSession();
        session.setAttribute("user", user);
        HttpResponse response = HttpResponse.found("/index.html");
        response.setCookie("JSESSIONID", session.getId());
        return response;
    }

}
