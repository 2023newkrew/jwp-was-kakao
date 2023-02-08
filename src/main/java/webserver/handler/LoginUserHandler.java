package webserver.handler;

import db.DataBase;
import model.User;
import webserver.HttpHeader;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Map;
import java.util.UUID;

public class LoginUserHandler implements Handler {
    @Override
    public Response apply(Request request) {
        Map<String, String> requestBody = request.getRequestBody();
        String userId = requestBody.get("userId");
        User foundUser = DataBase.findUserById(userId);
        if (foundUser.hasSamePassword(requestBody.get("password"))) {
            Response response = Response.found(new byte[0], request.findRequestedFileType(), "/index.html");
            response.addHeader(HttpHeader.SET_COOKIE, "JSESSIONID=" + UUID.randomUUID() + "; Path=/");
            return response;
        }
        return Response.found(new byte[0], request.findRequestedFileType(), "/user/login_failed.html");
    }
}
