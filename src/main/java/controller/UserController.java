package controller;

import model.User;
import service.UserService;

import java.io.DataOutputStream;

import static response.ResponseHeader.response302Header;

public class UserController {
    private final DataOutputStream dos;
    private final UserService userService;

    public UserController(DataOutputStream dos, UserService userService) {
        this.dos = dos;
        this.userService = userService;
    }

    public void saveUser(User user, String responseUrl){
        userService.saveUser(user);
        response302Header(dos, responseUrl);
    }
}
