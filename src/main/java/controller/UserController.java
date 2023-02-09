package controller;

import db.DataBase;
import dto.LoginDto;
import exceptions.AuthenticationException;
import http.HttpRequest;
import http.HttpResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.user.User;
import session.Session;
import session.SessionManager;
import utils.IOUtils;

import java.util.Map;
import java.util.UUID;

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
        String requestBody = request.getBody();
        Map<String, String> loginInfo = IOUtils.extractParamsMap(requestBody);
        LoginDto loginDto = LoginDto.of(loginInfo);

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
}
