package controller;

import db.Database;
import exception.WasException;
import model.User;
import utils.IOUtils;
import webserver.*;

import java.util.Map;
import java.util.Optional;

import static exception.ErrorCode.USER_NOT_EXIST;

public class UserLoginController implements Controller {

    private final Database db;
    private final SessionManager sessionManager;

    public UserLoginController(Database db, SessionManager sessionManager) {
        this.db = db;
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView run(HttpRequest request, HttpResponse response) {
        String requestBody = request.getRequestBody();
        Map<String, String> params = IOUtils.extractParams(requestBody);

        String userId = params.get("userId");
        String password = params.get("password");

        User user = Optional.ofNullable(db.findUserById(userId))
                .orElseThrow(() -> new WasException(USER_NOT_EXIST));

        HttpSession session = sessionManager.getSession(request, response);

        // 로그인 정보가 맞다면 세션에 결과 기록 후 index.html로 리다이렉트
        if (user.checkPassword(password)) {
            session.setAttribute("logined", true);
            return new ModelAndView("redirect:/index.html");
        }

        // 로그인 정보가 틀렸다면 세션에 결과 기록 후 login_failed.html로 리다이렉트
        session.setAttribute("logined", false);
        return new ModelAndView("redirect:/users/login_failed.html");
    }
}
