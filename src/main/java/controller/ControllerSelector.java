package controller;

import dto.BaseResponseDto;
import webserver.Request;
import webserver.StatusCode;

import java.util.Map;

import static webserver.HttpMethod.POST;

public class ControllerSelector {

    public final BaseController baseController = new BaseController();
    private final UserController userController = new UserController();

    public BaseResponseDto runMethod(Request request) {
        String contentType = request.getHeader().getContentType()
                .orElse("text/html");

        // baseController
        if (request.getHeader().checkRequest(POST, "/")) {
            return new BaseResponseDto(StatusCode.OK,
                    baseController.hello(),
                    contentType);
        }

        // userController
        if (request.getHeader().checkRequest(POST, "/user/create")) {
            Map<String, String> requestBody = request.convertBodyToMap();
            return new BaseResponseDto(StatusCode.FOUND,
                    userController.createUser(requestBody),
                    contentType);
        }

        return new BaseResponseDto(StatusCode.NOT_FOUND,
                String.format("%s %s METHOD NOT FOUND", request.getHeader().getHttpMethod(), request.getHeader().getUrl()),
                contentType);
    }
}
