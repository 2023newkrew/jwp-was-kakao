package webserver.controller;

import db.DataBase;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class UserCreateController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(UserCreateController.class);

    @Override
    public boolean isHandleable(HttpRequest request) {
        return request.getMethod().equals("POST") && request.getPath().equals("/user/create");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        Map<String, String> applicationForm = request.toApplicationForm();
        String userId = applicationForm.get("userId");
        String password = applicationForm.get("password");
        String name = applicationForm.get("name");
        String email = applicationForm.get("email");

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        logger.debug("{}", user);
        return HttpResponse.redirect("/index.html");
    }
}
