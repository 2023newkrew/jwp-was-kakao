package webserver.controller;

import db.DataBase;
import java.util.Map;
import model.HttpRequest;
import model.User;
import webserver.Controller;

public class UserSaveController implements Controller {

    @Override
    public String process(HttpRequest httpRequest) {
        Map<String, String> userInfo = httpRequest.getBody();

        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"),
                userInfo.get("email"));
        DataBase.addUser(user);

        return "";
    }

    @Override
    public boolean isRedirectRequired() {
        return true;
    }
}
