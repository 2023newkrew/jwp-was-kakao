package controller;

import db.Database;
import webserver.HttpCookie;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ModelAndView;

import java.util.Optional;

public class UserListController extends Controller {

    private final Database db;

    public UserListController(Database db) {
        this.db = db;
    }

    @Override
    protected ModelAndView run(HttpRequest request, HttpResponse response) {
        Optional<HttpCookie> cookieOpt = request.getRequestHeader().get("Cookie").map(HttpCookie::new);
        //TODO: 더 나은 Optional 활용법?
        if (cookieOpt.isPresent()) {
            Boolean logined = Boolean.valueOf(cookieOpt.get().get("logined").orElse("false"));
            if (logined) {
                ModelAndView modelAndView = new ModelAndView("/user/list");
                modelAndView.addAttribute("users", db.findAll());
                return modelAndView;
            }
        }
        return new ModelAndView("redirect:/user/login.html");
    }
}
