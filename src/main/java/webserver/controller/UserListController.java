package webserver.controller;

import db.DataBase;
import dto.UserDto;
import http.HttpResponse;
import http.ResponseStatus;
import http.request.HttpRequest;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserListController implements Controller {

    private static List<UserDto> getUserDtoList() {
        Collection<User> users = DataBase.findAll();
        long index = 1L;
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            System.out.println(user.getEmail());
            userDtoList.add(UserDto.from(index++, user));
        }
        return userDtoList;
    }

    @Override
    public String process(HttpRequest request, HttpResponse response) {

        if (!request.isLoggedIn()) {
            response.setResponseStatus(ResponseStatus.FOUND);
            return "/user/login.html";
        }

        List<UserDto> userDtoList = getUserDtoList();
        response.setModel(userDtoList);

        return "/user/list.html";
    }
}
