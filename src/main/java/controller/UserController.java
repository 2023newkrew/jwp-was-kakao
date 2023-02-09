package controller;

import db.DataBase;
import dto.LoginDto;
import enums.ContentType;
import exceptions.AuthenticationException;
import exceptions.AuthorizationException;
import http.HttpRequest;
import http.HttpResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.user.User;
import org.springframework.http.HttpStatus;
import session.Session;
import session.SessionManager;
import session.SessionUtils;
import utils.HandleBarsUtils;
import utils.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserController {
    private static final String CREATE_USER_REDIRECT_URI = "http://localhost:8080/index.html";
    private static UserController instance;
    private final SessionManager sessionManager = SessionManager.getInstance();

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public HttpResponse createUserGet(HttpRequest request) {
        String requestPath = request.getRequestPath();
        Map<String, String> userInfo = IOUtils.extractParamMapFromPath(requestPath);
        User user = User.from(userInfo);
        DataBase.addUser(user);

        return HttpResponse.create302FoundResponse(CREATE_USER_REDIRECT_URI);
    }

    public HttpResponse createUserPost(HttpRequest request) {
        String requestBody = request.getBody();
        Map<String, String> userInfo = IOUtils.extractParamsMap(requestBody);
        User user = User.from(userInfo);
        DataBase.addUser(user);

        return HttpResponse.create302FoundResponse(CREATE_USER_REDIRECT_URI);
    }

    public HttpResponse loginUserPost(HttpRequest request) {
        LoginDto loginDto = LoginDto.of(IOUtils.extractParamsMap(request.getBody()));

        User user = DataBase.findUserById(loginDto.getUserId())
                .orElseThrow(AuthenticationException::new);

        if (Boolean.FALSE.equals(user.isPasswordValid(user.getPassword()))) {
            throw new AuthenticationException();
        }

        Session session = new Session(UUID.randomUUID()
                .toString());
        session.setAttribute("user", user);
        sessionManager.add(session);

        HttpResponse response = HttpResponse.create302FoundResponse(CREATE_USER_REDIRECT_URI);
        response.addHeader(String.format("Set-Cookie: JSESSIONID=%s", session.getId()));
        return response;
    }

    public HttpResponse userListGet(HttpRequest request) throws IOException {
        SessionUtils.validateSession(request);

        List<User> userList = DataBase.findAll()
                .stream()
                .collect(Collectors.toList());

        String userListPage = HandleBarsUtils.setUserList(userList);

        return HttpResponse.of(HttpStatus.OK, ContentType.HTML, userListPage.getBytes());
    }
}
