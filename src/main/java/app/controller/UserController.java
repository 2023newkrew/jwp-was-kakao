package app.controller;

import app.controller.support.RequestParameters;
import app.service.UserService;
import infra.Controller;
import infra.http.header.ContentType;
import infra.http.header.Headers;
import infra.http.request.HttpRequest;
import infra.http.response.HttpResponse;
import infra.http.response.ResponseStatus;

public class UserController implements Controller {
    public static String PATH_CREATE = "/user/create";
    public static String REDIRECT_PATH = "/index.html";

    private UserService userService = new UserService();

    public HttpResponse response(HttpRequest request) {
        if (!request.isPOST()) {
            return new HttpResponse(ResponseStatus.BAD_REQUEST);
        }
        if (request.getUri().startsWith(PATH_CREATE)) {
            return this.createUser(request);
        }
        return new HttpResponse(ResponseStatus.BAD_REQUEST);
    }

    private HttpResponse createUser(HttpRequest request) {
        if (!request.getHeader(Headers.CONTENT_TYPE).equals(ContentType.X_WWW_FROM_URLENCODED.value())) {
            return new HttpResponse(ResponseStatus.BAD_REQUEST);
        }
        RequestParameters params = new RequestParameters(request.getBody().toString());
        userService.createUser(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email"));

        HttpResponse response = new HttpResponse(ResponseStatus.FOUND);
        response.setHeader(Headers.LOCATION, REDIRECT_PATH);
        return response;
    }
}
