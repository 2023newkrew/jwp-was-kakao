package controller;

import controller.annotation.CustomRequestBody;
import controller.annotation.CustomRequestMapping;
import controller.annotation.CustomRequestParams;
import db.DataBase;
import exception.UserNotFoundException;
import model.User;
import model.http.*;

import model.http.CustomHttpStatus;

public class UserController extends BaseController {

    @CustomRequestMapping(url = "/user/create", httpMethod = CustomHttpMethod.POST)
    public CustomHttpResponse createPOST(@CustomRequestBody User user) {
        DataBase.addUser(user);
        CustomHttpHeader headers = new CustomHttpHeader();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Location", "/index.html");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.FOUND)
                .headers(headers)
                .body("")
                .build();
    }

    @CustomRequestMapping(url = "/user/create", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse createGET(@CustomRequestParams User user) {
        DataBase.addUser(user);
        CustomHttpHeader headers = new CustomHttpHeader();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Location", "/index.html");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.FOUND)
                .headers(headers)
                .body("")
                .build();
    }

    @CustomRequestMapping(url = "/user/login", httpMethod = CustomHttpMethod.POST)
    public CustomHttpResponse login(@CustomRequestBody User user) {
        CustomHttpHeader headers = new CustomHttpHeader();
        User loginUser = DataBase.findUserById(user.getUserId()).orElseThrow(() -> new UserNotFoundException("아이디와 비밀번호가 일치하지 않습니다."));
        if (loginUser.hasSamePassword(user.getPassword())) {
            headers.put("Set-Cookie", new CustomHttpCookie().getCookie());
            headers.put("Location", "/index.html");
            return new CustomHttpResponse.Builder()
                    .httpStatus(CustomHttpStatus.FOUND)
                    .headers(headers)
                    .build();
        }
        headers.put("Location", "/user/login_failed.html");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.FOUND)
                .headers(headers)
                .build();
    }

}
