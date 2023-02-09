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
        response.found(new URI("/index.html"));
        response.send();
    }

    public void login(Request request, Response response) throws URISyntaxException, IOException {
        if (passIfAlreadyLogin(request, response)) return;
        String userId = request.getBodyValue("userId");
        String password = request.getBodyValue("password");
        User user = DataBase.findUserById(userId);
        if (Objects.isNull(user) || !user.getPassword().equals(password)) {
            sendLoginFailPage(response);
            return;
        }
        request.getSession().setAttribute("user", user);
        response.found(new URI("/index.html"));
        response.send();
    }

    public void sendPage(Request request, Response response) throws IOException, URISyntaxException {
        if (request.getPath().equals("/user/login.html") || request.getPath().equals("/user/form.html")) {
            if (passIfAlreadyLogin(request, response)) return;
        }
        byte[] body = request.getResponse();
        response.setBody(body, request.getAccept());
        response.send();
    }

    private boolean passIfAlreadyLogin(Request request, Response response) throws URISyntaxException, IOException {
        if (Objects.nonNull(request.getSession().getAttributes("user"))) {
            response.found(new URI("/index.html"));
            response.send();
            return true;
        }
        return false;
    }

    private void sendLoginFailPage(Response response) throws URISyntaxException, IOException {
        response.found(new URI("/user/login_failed.html"));
        response.send();
    }
}
