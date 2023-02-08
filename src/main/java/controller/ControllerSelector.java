package controller;

import static webserver.request.HttpMethod.GET;
import static webserver.request.HttpMethod.POST;

import dto.BaseResponseDto;
import java.util.Objects;
import webserver.request.Request;
import webserver.response.StatusCode;

public class ControllerSelector {

    public final BaseController baseController = BaseController.getInstance();
    private final UserController userController = UserController.getInstance();

    public BaseResponseDto runMethod(Request request) {

        // baseController
        if (request.getStartLine().getHttpMethod() == GET
            && Objects.equals(request.getStartLine().getUrl(), "/")) {
            return new BaseResponseDto(StatusCode.OK, baseController.hello());
        }

        // userController
        if (request.getStartLine().getHttpMethod() == POST
            && Objects.equals(request.getStartLine().getUrl(), "/user/create")) {

            return new BaseResponseDto(StatusCode.FOUND,
                userController.createUser(request.getBody()));
        }

        return new BaseResponseDto(StatusCode.NOT_FOUND, "");
    }
}
