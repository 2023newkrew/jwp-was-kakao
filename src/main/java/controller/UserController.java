package controller;

import model.User;
import service.UserService;

import java.io.DataOutputStream;

import static response.ResponseHeader.response302Header;

public class UserController {
    private final DataOutputStream dos;
    private final String requestUrl;

    public UserController(DataOutputStream dos, String requestUrl) {
        this.dos = dos;
        this.requestUrl = requestUrl;
    }

    public void saveUser(User user){
        UserService userService = new UserService();
        userService.saveUser(user);

        response302Header(dos, requestUrl);
    }
}
