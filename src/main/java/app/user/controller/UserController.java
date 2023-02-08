package app.user.controller;

import static app.user.controller.UserControllerConstants.INDEX_HTML_URL;
import static app.user.controller.UserControllerConstants.LOGIN_HTML_URL;
import static app.user.controller.UserControllerConstants.USER_FORM_HTML_URL;
import static app.user.view.UserListView.make200ResponseUserListView;
import static infra.utils.response.Response302HeaderUtils.make302ResponseHeader;
import static infra.utils.response.ResponseUtils.make200TemplatesResponse;

import app.excpetion.DuplicateException;
import app.infra.MyAppRequest;
import app.user.dto.UserCreateDto;
import app.user.dto.UserResponseDto;
import app.user.service.UserService;
import infra.controller.MyController;
import infra.controller.MyGetMapping;
import infra.controller.MyPostMapping;
import infra.utils.request.MyRequest;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class UserController implements MyController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MyGetMapping(paths = {"/user/list", "/user/list.html"})
    public void userList(MyRequest request, DataOutputStream dataOutputStream) {
        MyAppRequest myAppRequest = MyAppRequest.of(request);
        if (Objects.nonNull(myAppRequest.getLoginUser())) {
            List<UserResponseDto> users = userService.findAll();
            make200ResponseUserListView(request.getHeader(HttpHeaders.ACCEPT), dataOutputStream, users, logger);
            return;
        }
        make302ResponseHeader(dataOutputStream, LOGIN_HTML_URL.url(), logger);
    }

    @MyGetMapping(paths = "/user/login.html")
    public void loginForm(MyRequest request, DataOutputStream dataOutputStream) {
        MyAppRequest myAppRequest = MyAppRequest.of(request);
        if (Objects.nonNull(myAppRequest.getLoginUser())) {
            make302ResponseHeader(dataOutputStream, INDEX_HTML_URL.url(), logger);
            return;
        }
        make200TemplatesResponse(request.getPath(), request.getHeader(HttpHeaders.ACCEPT), dataOutputStream, logger);
    }

    @MyPostMapping(paths = "/user/create")
    public void createUser(MyRequest request, DataOutputStream dataOutputStream) {
        MyAppRequest myAppRequest = MyAppRequest.of(request);
        if (Objects.nonNull(myAppRequest.getLoginUser())) {
            make302ResponseHeader(dataOutputStream, INDEX_HTML_URL.url(), logger);
            return;
        }
        try {
            userService.create(createUserCreateDto(request));
            make302ResponseHeader(dataOutputStream, INDEX_HTML_URL.url(), logger);
        } catch (DuplicateException e) {
            logger.error(e.getMessage());
            make302ResponseHeader(dataOutputStream, USER_FORM_HTML_URL.url(), logger);
        }
    }

    @MyGetMapping(paths = "/user/form.html")
    public void form(MyRequest request, DataOutputStream dataOutputStream) {
        MyAppRequest myAppRequest = MyAppRequest.of(request);
        if (Objects.nonNull(myAppRequest.getLoginUser())) {
            make302ResponseHeader(dataOutputStream, INDEX_HTML_URL.url(), logger);
            return;
        }
        make200TemplatesResponse(request.getPath(), request.getHeader(HttpHeaders.ACCEPT), dataOutputStream, logger);
    }

    @MyGetMapping(paths = "/user/login_failed.html")
    public void loginFail(MyRequest request, DataOutputStream dataOutputStream) {
        MyAppRequest myAppRequest = MyAppRequest.of(request);
        if (Objects.nonNull(myAppRequest.getLoginUser())) {
            make302ResponseHeader(dataOutputStream, INDEX_HTML_URL.url(), logger);
            return;
        }
        make200TemplatesResponse(request.getPath(), request.getHeader(HttpHeaders.ACCEPT), dataOutputStream, logger);
    }

    private UserCreateDto createUserCreateDto(MyRequest myRequest) {
        return UserCreateDto.builder()
                .userId(myRequest.getParam("userId"))
                .name(myRequest.getParam("name"))
                .email(myRequest.getParam("email"))
                .password(myRequest.getParam("password"))
                .build();
    }
}
