package controller;

import java.util.Map;
import service.UserService;

public class UserController {

    private static final UserController instance = new UserController();
    private static final UserService userService = UserService.getInstance();

    private UserController() {
    }

    public static UserController getInstance() {
        return instance;
    }

    public String createUser(Map<String, String> params) {
        userService.createUser(params);
        return "";
    }
}
