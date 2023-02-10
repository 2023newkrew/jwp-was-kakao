package webserver.controller;

import db.DataBase;
import dto.UserDto;
import http.Cookie;
import http.HttpResponse;
import http.request.HttpRequest;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserListController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        boolean isLoggedIn = request.getRequestHeader().getCookie().hasCookie(Cookie.SESSION_ID_NAME);
        if (!isLoggedIn) {
            return "/user/login.html";
        }

        Collection<User> users = DataBase.findAll();
        long index = 1L;
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            System.out.println(user.getEmail());
            userDtoList.add(UserDto.from(index++, user));
        }
        response.setModel(userDtoList);

        return "/user/list.html";
    }
}
