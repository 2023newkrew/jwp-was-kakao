package webserver;

import constant.HttpMethod;
import db.DataBase;
import model.User;

import java.io.DataOutputStream;
import java.util.Map;

import static utils.ResponseUtils.*;

public class UserController extends ApiController{
    private static final UserController instance;
    private UserController() {}

    static {
        try {
            instance = new UserController();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static UserController getInstance() {
        return instance;
    }

    @Api(method = HttpMethod.POST, url = "/user/create")
    public Object register(HttpRequest httpRequest, DataOutputStream dos) {
        Map<String, String> requestBody = httpRequest.getBody();
        DataBase.addUser(new User(
                requestBody.get("userId"),
                requestBody.get("password"),
                requestBody.get("name"),
                requestBody.get("email"))
        );

        response302Header(dos, "/index.html");
        return null;
    }
}
