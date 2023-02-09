package webserver.controller;

import db.DataBase;
import java.util.Map;
import java.util.UUID;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController implements Controller {
    @Override
    public boolean isHandleable(HttpRequest request) {
        return request.getMethod().equals("POST") && request.getPath().equals("/user/login");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        Map<String, String> applicationForm = request.toApplicationForm();

        String id = applicationForm.get("userId");
        String password = applicationForm.get("password");
        User user = DataBase.findUserById(id);

        if (user == null || !user.getPassword().equals(password)) {
            return HttpResponse.redirect("/user/login_failed.html");
        }

        String sessionId = UUID.randomUUID().toString();
        Map<String, String> headers = Map.of(
                "Set-Cookie", "JSESSIONID=" + sessionId + "; Path=/"
        );
        return HttpResponse.redirect(headers, "/index.html");
    }
}
