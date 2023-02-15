package controller;

import controller.annotation.CustomRequestBody;
import controller.annotation.CustomRequestHeader;
import controller.annotation.CustomRequestMapping;
import controller.annotation.CustomRequestParams;
import model.User;
import model.http.*;
import service.UserService;

import java.io.IOException;

public class UserController implements BaseController {

    private final UserService userService = new UserService();

    @CustomRequestMapping(url = "/user/create", httpMethod = CustomHttpMethod.POST)
    public CustomHttpResponse createPOST(@CustomRequestBody User user) {
        userService.join(user);
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
        userService.join(user);
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
        User loginUser = userService.findUserById(user);
        if (loginUser.hasSamePassword(user.getPassword())) {
            CustomHttpCookie cookie = new CustomHttpCookie();
            headers.put("Set-Cookie", cookie.getCookie());
            CustomHttpSession session = new CustomHttpSession(cookie.getjSessionId());
            session.setAttribute("loginUser", loginUser);
            headers.put("Location", "/index.html");
            return new CustomHttpResponse.Builder()
                    .httpStatus(CustomHttpStatus.FOUND)
                    .headers(headers)
                    .body("")
                    .build();
        }
        headers.put("Location", "/user/login_failed.html");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.FOUND)
                .headers(headers)
                .body("")
                .build();
    }

    @CustomRequestMapping(url = "/user/list", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse list(@CustomRequestHeader CustomHttpHeader requestHeader) throws IOException {
        CustomHttpHeader headers = new CustomHttpHeader();
        if (!requestHeader.isLogined()) {
            headers.put("Location", "/user/login.html");
            return new CustomHttpResponse.Builder()
                    .httpStatus(CustomHttpStatus.FOUND)
                    .headers(headers)
                    .body("")
                    .build();
        }
        headers.put("Content-Type", "text/html;charset=utf-8");

        String body = userService.getUserListHtml();

        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.OK)
                .headers(headers)
                .body(body)
                .build();
    }

}
