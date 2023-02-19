package controller;

import db.Database;
import webserver.*;

public class UserListController implements Controller {

    private final Database db;
    private final SessionManager sessionManager;

    public UserListController(Database db, SessionManager sessionManager) {
        this.db = db;
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView run(HttpRequest request, HttpResponse response) {
        HttpSession session = sessionManager.getSession(request, response);
        Object logined = session.getAttribute("logined");

        if (logined != null && logined.equals(true)) {
            ModelAndView modelAndView = new ModelAndView("/user/list");
            modelAndView.addAttribute("users", db.findAll());
            return modelAndView;
        }

        return new ModelAndView("redirect:/user/login.html");
    }
}
