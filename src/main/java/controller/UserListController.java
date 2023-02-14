package controller;

import java.io.IOException;
import java.util.List;
import model.User;
import utils.RenderUtils;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import service.UserService;

public class UserListController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        if (httpRequest.getHttpSession().getAttribute("user") != null) {
            try {
                List<User> userList = UserService.getUserList();
                String userListPage = RenderUtils.renderListData("/user/list", userList);
                return HttpResponse.ok(httpRequest, userListPage.getBytes(), "text/html;charset=utf-8");
            } catch (IOException e) {
                return HttpResponse.pageNotFound();
            }
        }
        return HttpResponse.redirect(httpRequest, "http://localhost:8080/user/login.html");
    }
}
