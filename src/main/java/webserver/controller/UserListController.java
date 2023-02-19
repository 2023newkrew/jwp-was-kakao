package webserver.controller;

import static webserver.controller.UserLoginController.REDIRECTION_HOME;

import db.SessionStorage;
import db.UserStorage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.MyHttpRequest;
import model.MyHttpResponse;
import model.MyModelAndView;
import model.Session;
import org.springframework.http.HttpStatus;
import webserver.Controller;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserListController implements Controller {

    public static final String URL = "/user/list.html";

    private static final UserListController INSTANCE = new UserListController();

    public static UserListController getInstance() {
        return INSTANCE;
    }

    @Override
    public MyModelAndView process(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        if (!doAuthenticate(httpRequest)) {
            httpResponse.setStatus(HttpStatus.TEMPORARY_REDIRECT);
            httpResponse.setLocation(REDIRECTION_HOME);
            return new MyModelAndView();
        }

        httpResponse.setStatus(HttpStatus.OK);

        MyModelAndView mav = new MyModelAndView("./templates", httpRequest.getUrl());
        mav.addAttribute("users", UserStorage.findAll());
        return mav;
    }

    private boolean doAuthenticate(MyHttpRequest httpRequest) {
        String sessionId = httpRequest.getSession();
        Session session = SessionStorage.findSession(sessionId);
        return session.getAttribute("user") != null;
    }
}
