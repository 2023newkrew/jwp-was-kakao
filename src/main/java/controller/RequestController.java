package controller;

import db.DataBase;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import model.Request;
import model.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import webserver.Session;
import webserver.SessionManager;

public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    public void createUser(Request request, Response response) throws URISyntaxException, IOException {
        User user = new User(
                request.getBodyValue("userId"),
                request.getBodyValue("password"),
                request.getBodyValue("name"),
                request.getBodyValue("email")
        );
        DataBase.addUser(user);
        response.setStatus(HttpStatus.FOUND);
        response.setHeader("Location", new URI("/index.html").toString());
        response.send();
    }

    public void login(Request request, Response response) throws URISyntaxException, IOException {
        Session session = SessionManager.findSession(request.getCookie("JSESSIONID"));
        if (Objects.nonNull(session.getAttributes("user"))) {
            passBecauseAlreadyLogin(response);
            return;
        }
        String userId = request.getBodyValue("userId");
        String password = request.getBodyValue("password");
        User user = DataBase.findUserById(userId);
        if (Objects.isNull(user) || !user.getPassword().equals(password)) {
            response.setStatus(HttpStatus.FOUND);
            response.setHeader("Location", new URI("/user/login_failed.html").toString());
            response.send();
            return;
        }
        session.setAttribute("user", user);
        response.setStatus(HttpStatus.FOUND);
        response.setHeader("Location", new URI("/index.html").toString());
        response.send();
    }

    public void sendPage(Request request, Response response) throws IOException, URISyntaxException {
        Session session = SessionManager.findSession(request.getCookie("JSESSIONID"));
        if (request.getPath().equals("/user/login.html") || request.getPath().equals("/user/form.html")) {
            if (Objects.nonNull(session.getAttributes("user"))) {
                passBecauseAlreadyLogin(response);
                return;
            }
        }
        byte[] body = request.getResponse();
        response.setBody(body, request.getAccept());
        response.send();
    }

    private void passBecauseAlreadyLogin(Response response) throws URISyntaxException, IOException {
        response.setStatus(HttpStatus.FOUND);
        response.setHeader("Location", new URI("/index.html").toString());
        response.send();
    }
}
