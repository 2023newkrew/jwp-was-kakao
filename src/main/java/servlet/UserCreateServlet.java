package servlet;

import db.DataBase;
import exception.BadRequestException;
import http.HttpStatus;
import http.request.Request;
import http.request.RequestBody;
import http.request.RequestData;
import http.request.RequestParams;
import http.response.Response;
import model.User;

import java.util.NoSuchElementException;

@ServletMapping(uri = "/user/create")
public class UserCreateServlet implements Servlet {
    public static final String REQUEST_PATH = "/user/create";
    private static class UserCreateServletHolder {
        private static final UserCreateServlet instance = new UserCreateServlet();
    }

    private UserCreateServlet() {
    }

    public static UserCreateServlet getInstance() {
        return UserCreateServletHolder.instance;
    }

    @Override
    public Response doGet(Request request) {
        RequestParams params = request.getUri().getParams().orElseThrow(BadRequestException::new);
        User newUser = createNewUser(params);
        DataBase.addUser(newUser);
        return Response.builder()
                .httpVersion(request.getVersion())
                .httpStatus(HttpStatus.FOUND)
                .location("/index.html")
                .build();
    }

    @Override
    public Response doPost(Request request) {
        RequestBody body = request.getBody();
        User newUser = createNewUser(body);
        DataBase.addUser(newUser);
        return Response.builder()
                .httpVersion(request.getVersion())
                .httpStatus(HttpStatus.FOUND)
                .location("/index.html")
                .build();
    }

    private User createNewUser(RequestData data) {
        try {
            return new User(
                    data.get("userId").get(),
                    data.get("password").get(),
                    data.get("name").get(),
                    data.get("email").get()
            );
        } catch (NoSuchElementException e) {
            throw new BadRequestException();
        }
    }
}
