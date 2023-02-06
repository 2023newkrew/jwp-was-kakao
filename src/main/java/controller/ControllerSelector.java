package controller;

import dto.BaseResponseDto;
import webserver.Request;
import webserver.StatusCode;

import java.util.Objects;

import static webserver.HttpMethod.GET;

public class ControllerSelector {

    public final BaseController baseController = new BaseController();
    private final UserController userController = new UserController();

    public BaseResponseDto runMethod(Request request) {
        // baseController
        if (request.getHttpMethod() == GET
                && Objects.equals(request.getUrl(), "/")) {
            return new BaseResponseDto(StatusCode.OK, baseController.hello());
        }
        // userController
        if (request.getHttpMethod() == GET
                && Objects.equals(request.getUrl(), "/user/create")) {
            return new BaseResponseDto(StatusCode.CREATED,
                    userController.createUser(request.getQueryParams()));
        }

        return new BaseResponseDto(StatusCode.NOT_FOUND, "");
    }
}
