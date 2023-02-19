package webserver.controller;

import static webserver.controller.UserLoginController.REDIRECTION_HOME;

import db.UserStorage;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.MyHttpRequest;
import model.MyHttpResponse;
import model.MyModelAndView;
import model.User;
import org.springframework.http.HttpStatus;
import webserver.Controller;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSaveController implements Controller {

    public static final String URL = "/user/create";

    private static final UserSaveController INSTANCE = new UserSaveController();

    public static UserSaveController getInstance() {
        return INSTANCE;
    }

    @Override
    public MyModelAndView process(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        Map<String, String> userInfo = httpRequest.getBody();

        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"),
                userInfo.get("email"));
        UserStorage.addUser(user);
        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.setLocation(REDIRECTION_HOME);

        return new MyModelAndView();
    }
}
