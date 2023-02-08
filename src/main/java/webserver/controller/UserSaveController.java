package webserver.controller;

import db.UserStorage;
import java.util.Map;
import model.MyHttpRequest;
import model.User;
import webserver.Controller;

public class UserSaveController implements Controller {

    @Override
    public String process(MyHttpRequest httpRequest) {
        Map<String, String> userInfo = httpRequest.getBody();

        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"),
                userInfo.get("email"));
        UserStorage.addUser(user);

        return "";
    }

    @Override
    public boolean isRedirectRequired() {
        return true;
    }
}
