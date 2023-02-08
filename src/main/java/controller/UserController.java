package controller;

import service.UserService;

import java.util.Map;

public class UserController {

    private final UserService userService = new UserService();

    public String createUser(Map<String, String> params) {
        userService.createUser(params);
        return "";
    }
}
