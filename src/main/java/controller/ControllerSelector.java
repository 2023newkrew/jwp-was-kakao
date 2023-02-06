package controller;

import dto.BaseResponseDto;
import webserver.Request;
import webserver.StatusCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static webserver.HttpMethod.GET;
import static webserver.HttpMethod.POST;

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
        if (request.getHttpMethod() == POST
                && Objects.equals(request.getUrl(), "/user/create")) {
            Map<String, String> requestBody = extractBody(request.getBody());

            return new BaseResponseDto(StatusCode.FOUND,
                    userController.createUser(requestBody));
        }

        return new BaseResponseDto(StatusCode.NOT_FOUND, "");
    }

    private static Map<String, String> extractBody(String rawBody) {
        Map<String, String> body = new HashMap<>();
        Arrays.stream(rawBody.split("&"))
                .forEach(v -> {
                    String[] kv = v.split("=");
                    body.put(kv[0], kv[1]);
                });

        return body;
    }
}
