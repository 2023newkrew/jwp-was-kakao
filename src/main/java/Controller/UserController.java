package Controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import dto.UserLoginDto;
import model.User;
import service.UserService;
import session.HttpCookie;
import session.Session;
import session.SessionManager;
import webserver.ResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class UserController {
    private static final UserController INSTANCE = new UserController();
    private static final String SESSION_KEY = "JSESSIONID";

    private final UserService userService;

    private UserController(){
        this.userService = UserService.getInstance();
    }

    public static UserController getInstance(){
        return INSTANCE;
    }

    public void login(DataOutputStream dos, HashMap<String, String> requestBody){
        UserLoginDto userLoginDto = requestBodyToUserLoginDto(requestBody);
        if(userService.login(userLoginDto)){
            Session session = new Session(UUID.randomUUID().toString());
            session.setAttribute("userId", userLoginDto.getUserId());
            SessionManager.add(session);

            ResponseUtils.response200Header(dos);
            ResponseUtils.setCookie(dos, SESSION_KEY, session.getId());
        }else{
            ResponseUtils.response400(dos);
        }
    }

    private UserLoginDto requestBodyToUserLoginDto(HashMap<String, String> requestBody){
        return new UserLoginDto(
                requestBody.get("userId"),
                requestBody.get("password")
        );
    }

    public void createUser(DataOutputStream dos, HashMap<String, String> requestBody){
        User user = requestBodyToUser(requestBody);
        userService.createUser(user);

        ResponseUtils.response302(dos, "/index.html");
    }

    private User requestBodyToUser(HashMap<String, String> requestBody){
        return new User(
                requestBody.get("userId"),
                requestBody.get("password"),
                requestBody.get("name"),
                requestBody.get("email")
        );
    }

    public void getUserList(DataOutputStream dos, String path, HttpCookie httpCookie) throws IOException {
        byte[] body = new byte[0];
        String sessionId = httpCookie.getCookieValueByKey(SESSION_KEY);

        if(SessionManager.findSession(sessionId) == null){
            ResponseUtils.response302(dos, "/user/login.html");
        }else{
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/templates");
            loader.setSuffix("");
            Handlebars handlebars = new Handlebars(loader);
            handlebars.registerHelper("incrementVal", (context, options) -> (Integer) context + 1);

            Template template = handlebars.compile(path);

            body = template.apply(DataBase.findAll()).getBytes();
            ResponseUtils.response200Header(dos, body.length, path);
        }

        ResponseUtils.responseBody(dos, body);
    }
}
