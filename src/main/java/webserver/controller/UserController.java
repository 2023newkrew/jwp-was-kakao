package webserver.controller;

import constant.PathConstant;
import db.DataBase;
import model.annotation.Api;
import model.annotation.ApiController;
import model.dto.user.UserListViewDto;
import model.enumeration.ContentType;
import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.response.HttpResponse;
import utils.builder.ResponseBuilder;
import webserver.dao.UserDao;
import webserver.infra.ViewResolver;
import webserver.service.UserService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static constant.DefaultConstant.*;
import static constant.PathConstant.*;
import static utils.utils.LoginUtils.*;
import static utils.utils.TemplateUtils.*;

@ApiController
public class UserController {
    private static final UserController instance;

    static {
        instance = new UserController(new UserService(new UserDao()));
    }

    private final UserService userService;
    private final int INITIAL_LIST_INDEX = 1;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    public static UserController getInstance() {
        return instance;
    }

    @Api(method = HttpMethod.POST, url = "/user/create", consumes = ContentType.APPLICATION_URL_ENCODED)
    public HttpResponse register(HttpRequest request) {
        userService.addUser(request);

        return ResponseBuilder.found(DEFAULT_PAGE);
    }

    @Api(method = HttpMethod.GET, url = "/user/list.html")
    public HttpResponse showUserList(HttpRequest request) {
        if (!isLogin(request)) {
            return ResponseBuilder.found(LOGIN_PAGE);
        }

        return ViewResolver.resolve(
                createTemplate(
                        "/user/list",
                        mappingAllUserToUserViewDto(new AtomicInteger(INITIAL_LIST_INDEX)))
        );
    }

    private List<UserListViewDto> mappingAllUserToUserViewDto(AtomicInteger index) {
        return DataBase.findAll().stream()
                .map(user -> new UserListViewDto(
                        (long) index.getAndIncrement(),
                        user.getUserId(),
                        user.getName(),
                        user.getEmail()))
                .collect(Collectors.toList());
    }
}
