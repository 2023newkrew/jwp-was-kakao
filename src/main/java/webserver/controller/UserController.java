package webserver.controller;

import db.DataBase;
import model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.controller.annotation.Handler;
import webserver.controller.annotation.RequestController;
import webserver.controller.support.TemplateView;
import webserver.controller.support.TemplateViewResolver;
import webserver.http.Session;
import webserver.http.request.Request;
import webserver.http.request.support.FormData;
import webserver.http.request.support.QueryParameters;
import webserver.http.response.Response;

@RequestController
public class UserController {

    @Handler(method = HttpMethod.GET, value = "/user/create")
    public void createUserGet(Request req, Response res) {
        QueryParameters parameters = new QueryParameters(req.getURL());
        User user = new User(
                parameters.getValue("userId"),
                parameters.getValue("password"),
                parameters.getValue("name"),
                parameters.getValue("email")
        );
        DataBase.addUser(user);
        res.setStatus(HttpStatus.OK);
    }

    @Handler(method = HttpMethod.POST, value = "/user/create")
    public void createUserPost(Request req, Response res) {
        FormData formData = new FormData(req.getBody());
        User user = new User(
                formData.getValue("userId"),
                formData.getValue("password"),
                formData.getValue("name"),
                formData.getValue("email")
        );
        DataBase.addUser(user);
        res.setRedirection("/index.html");
    }

    @Handler(method = HttpMethod.POST, value = "/user/login")
    public void login(Request req, Response res) {
        FormData formData = new FormData(req.getBody());
        User user = DataBase.findUserById(formData.getValue("userId"));
        if (user == null || !user.checkPasswordMatch(formData.getValue("password"))) {
            res.setRedirection("/user/login_failed.html");
            return;
        }
        Session session = req.getSession();
        session.setAttribute("user", user);
        res.setRedirection("/index.html");
    }

    @Handler(method = HttpMethod.GET, value = "/user/logout")
    public void logout(Request req, Response res) {
        req.getSession().invalidate();
        System.out.println("SESSION DELETE!!!");
        res.setRedirection("/user/login.html");
    }

    @Handler(method = HttpMethod.GET, value = "/user/list")
    public void listUser(Request req, Response res) throws Exception {
        TemplateViewResolver viewResolver = new TemplateViewResolver();
        String viewName = "/user/list";
        if (!req.isLogined()) {
            viewName = "/user/login";
        }
        req.addAttribute("users", DataBase.findAll());
        TemplateView view = viewResolver.resolveViewName(viewName);
        view.render(req, res);
        res.setStatus(HttpStatus.OK);
    }
}
