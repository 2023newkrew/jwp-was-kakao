package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import controller.annotation.CustomRequestBody;
import controller.annotation.CustomRequestHeader;
import controller.annotation.CustomRequestMapping;
import controller.annotation.CustomRequestParams;
import db.DataBase;
import exception.UserNotFoundException;
import model.User;
import model.http.*;

import java.io.IOException;
import java.util.Collection;

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

        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        Template template = handlebars.compile("user/list");
        Collection<User> users = DataBase.findAll();
        String page = template.apply(users);

        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.OK)
                .headers(headers)
                .body(page)
                .build();
    }

}
