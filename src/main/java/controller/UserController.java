package controller;

import controller.annotation.CustomRequestBody;
import controller.annotation.CustomRequestMapping;
import controller.annotation.CustomRequestParams;
import db.DataBase;
import model.*;
import model.http.CustomHttpHeader;
import model.http.CustomHttpMethod;
import model.http.CustomHttpResponse;
import model.http.CustomHttpStatus;

public class UserController extends BaseController {

    @CustomRequestMapping(url = "/user/create", httpMethod = CustomHttpMethod.POST)
    public CustomHttpResponse createPOST(@CustomRequestBody User user) {
        DataBase.addUser(user);
        CustomHttpHeader headers = new CustomHttpHeader();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Location", "/index.html");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.FOUND)
                .headers(headers)
                .body("")
                .build();
    }

    @CustomRequestMapping(url = "/user/create", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse createGET(@CustomRequestParams User user) {
        DataBase.addUser(user);
        CustomHttpHeader headers = new CustomHttpHeader();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Location", "/index.html");
        return new CustomHttpResponse.Builder()
                .httpStatus(CustomHttpStatus.FOUND)
                .headers(headers)
                .body("")
                .build();
    }

}
