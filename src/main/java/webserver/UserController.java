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
        instance = new UserController();
    }
    public static UserController getInstance() {
        return instance;
    }

    @Api(method = HttpMethod.POST, url = "/user/create")
    public void register(HttpRequest request, DataOutputStream dos) {
        new UserDao().saveUser(request);

        response302Header(dos, "/index.html");
    }
}
