package controller;

import db.Database;
import model.User;
import utils.IOUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ModelAndView;

import java.util.Map;

/**
 * 관련 URI: /user/create
 * 유저 생성 후 index.html로 리다이렉트
 */
public class UserCreateController extends Controller {

    private final Database db;

    public UserCreateController(Database db) {
        this.db = db;
    }

    @Override
    protected ModelAndView run(HttpRequest request, HttpResponse response) {
        Map<String, String> createUserReqMap = IOUtils.extractParams(request.getRequestBody());
        db.addUser(new User(
                createUserReqMap.get("userId"),
                createUserReqMap.get("password"),
                createUserReqMap.get("name"),
                createUserReqMap.get("email")
        ));

        return new ModelAndView("redirect:/index.html");
    }
}
