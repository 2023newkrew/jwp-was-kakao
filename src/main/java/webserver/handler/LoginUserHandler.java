package webserver.handler;

import db.DataBase;
import model.User;
import webserver.common.HttpHeader;
import webserver.common.HttpSession;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class LoginUserHandler implements Handler {
    @Override
    public Response apply(Request request) {
        User foundUser = findUser(request);
        if (Objects.isNull(foundUser)) {
            return Response.found(
                new byte[0],
                request.findRequestedFileType(),
                "/user/login_failed.html"
            );
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", foundUser);

        Response response = Response.found(
            new byte[0],
            request.findRequestedFileType(),
            "/index.html"
        );
        response.setHeader(HttpHeader.SET_COOKIE, "JSESSIONID=" + session.getId() + "; Path=/");
        return response;
    }

    private User findUser(Request request) {
        Map<String, String> requestBody = request.getRequestBody();
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");
        return DataBase.findUserByIdAndPassword(userId, password);
    }
}
