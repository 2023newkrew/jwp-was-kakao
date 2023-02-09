package webserver.handler;

import db.DataBase;
import java.util.Map;
import java.util.UUID;
import model.User;
import webserver.response.HttpResponse;
import webserver.request.HttpRequest;

public class LoginHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        Map<String, String> body = request.getBodyLikeQueryParams();
        User user = DataBase.findUserById(body.get("userId"));
        if (user == null || !user.checkPassword(body.get("password"))) {
            return HttpResponse.found(new byte[0], request.getFilenameExtension(), "/user/login_failed.html");
        }
        HttpResponse response = HttpResponse.found(new byte[0], request.getFilenameExtension(), "/index.html");
        if (request.getHeaders().getCookie().get("JSESSIONID").isEmpty()) {
            response.setCookie("JSESSIONID", UUID.randomUUID().toString());
        }
        return response;
    }
}
