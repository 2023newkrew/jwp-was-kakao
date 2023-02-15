package controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import model.User;
import model.dto.Cookie;
import model.dto.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.request.HttpRequest;
import webserver.request.MyHeaders;
import webserver.request.MyParams;
import utils.UserFactory;
import webserver.response.ResponseEntity;
import webserver.session.Session;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.stream.Collectors;

import static db.DataBase.*;
import static webserver.session.SessionManager.*;

public class UserController  implements MyController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public boolean canHandle(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        return path.startsWith("/user");
    }

    @Override
    public ResponseEntity handle(HttpRequest httpRequest, DataOutputStream dataOutputStream) {
        String path = httpRequest.getPath();

        if(path.equals("/user/form.html") && httpRequest.compareMethod("GET")){
            return createForm(httpRequest);
        }

        if(path.equals("/user/login.html") && httpRequest.compareMethod("GET")){
            return loginForm(httpRequest);
        }

        if(path.equals("/user/list") && httpRequest.compareMethod("GET")){
            return getUserList(httpRequest);
        }

        if(path.equals("/user/login_failed.html") && httpRequest.compareMethod("GET")){
            return loginFailForm(httpRequest);
        }

        if(path.equals("/user/login") && httpRequest.compareMethod("GET")){
            return alreadyLogin(httpRequest);
        }

        if(path.equals("/user/create") && httpRequest.compareMethod("POST")){
            return createUser(httpRequest);
        }

        if(path.equals("/user/login") && httpRequest.compareMethod("POST")){
            return login(httpRequest);
        }

        return ResponseEntity.builder()
                .build();
    }

    private ResponseEntity setRedirectResponse(HttpRequest httpRequest, String redirectUrl){
        String cookie = httpRequest.getCookie().toString();
        return ResponseEntity.builder()
                .status(302)
                .redirectUrl(redirectUrl)
                .cookie(cookie)
                .build();
    }
    private ResponseEntity alreadyLogin(HttpRequest httpRequest) {
        return setRedirectResponse(httpRequest, "/index.html");
    }

    private ResponseEntity login(HttpRequest httpRequest) {
        MyParams params = httpRequest.getParams();
        String userId = params.get("userId");
        String password = params.get("password");

        if(isUser(userId, password)){
            return loginSuccess(httpRequest, userId);
        }
        return loginFail(httpRequest);
    }

    private ResponseEntity loginSuccess(HttpRequest httpRequest, String userId){
        // 쿠키 생성
        Cookie cookie = new Cookie();
        cookie.generateUUID();
        httpRequest.setCookie(cookie);

        // 세션 저장
        saveSession(cookie.toString(), userId);
        return setRedirectResponse(httpRequest,"/index.html");
    }

    private void saveSession(String sessionId, String userId){
        Session session = new Session(sessionId);
        session.setAttribute("user", findUserById(userId));
        add(session);
    }

    private ResponseEntity loginFail(HttpRequest httpRequest){
        return setRedirectResponse(httpRequest, "/user/login_failed.html");
    }

    private ResponseEntity loginFailForm(HttpRequest httpRequest){
        return setTemplatePathToBody(httpRequest);
    }

    // 로그인 검증 로직
    private boolean isUser(String userId, String password){
        User user = findUserById(userId);
        if(Objects.isNull(user) || !password.equals(user.getPassword())) return false;
        return true;
    }

    private ResponseEntity getUserList(HttpRequest httpRequest){
        String cookie = httpRequest.getCookie().toString();
        // login이 안되어있다면
        if(!cookie.startsWith("JSESSIONID")){
            String path = "/user/login.html";
            httpRequest.setPath(path);
            return loginForm(httpRequest);
        }
        return userList(cookie);
    }

    private ResponseEntity loginForm(HttpRequest httpRequest){
        String cookie = httpRequest.getCookie().toString();
        if(!cookie.startsWith("JSESSIONID")) {
            return setTemplatePathToBody(httpRequest);
        }
        // 로그인이 되어 있다면, index.html로 redirect
        return setRedirectResponse(httpRequest, "/index.html");
    }

    private ResponseEntity userList(String cookie) {
        byte[] body = null;

        try{
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/templates");
            loader.setSuffix(".hbs");
            Handlebars handlebars = new Handlebars(loader);

            Template template = handlebars.compile("user/list");

            // handlebars 모델 전송용 dto
            Users users = new Users(findAll().stream().collect(Collectors.toList()));

            String userList = template.apply(users);
            body = userList.getBytes();
        } catch(IOException e){
            logger.error(e.getMessage());
        }

        return ResponseEntity.builder()
                .status(200)
                .body(body)
                .cookie(cookie)
                .build();
    }

    private ResponseEntity createUser(HttpRequest httpRequest){
        MyParams params = httpRequest.getParams();
        // Memory DB에 유저 데이터 저장
        addUser(UserFactory.createUser(params));
        // 회원가입 시 자동 로그인이 되어야 되기 때문에, login 설정
        return loginSuccess(httpRequest, params.get("userId"));
    }

    private ResponseEntity createForm(HttpRequest httpRequest){
        return setTemplatePathToBody(httpRequest);
    }

    private ResponseEntity setTemplatePathToBody(HttpRequest httpRequest){
        String path = httpRequest.getPath();
        String cookie = httpRequest.getCookie().toString();
        byte[] body = null;

        try {
            body = FileIoUtils.loadFileFromClasspath("templates" + path);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.builder()
                .status(200)
                .body(body)
                .cookie(cookie)
                .build();
    }
}
