package controller;

import dto.BaseResponseDto;
import webserver.Request;
import webserver.StatusCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static webserver.HttpMethod.GET;
import static webserver.HttpMethod.POST;

public class ControllerSelector {

    public final BaseController baseController = new BaseController();
    private final UserController userController = new UserController();

    public BaseResponseDto runMethod(Request request) {
        String contentType = "text/html";
        if (request.getHeader().getHeaders().containsKey("Accept")) {
            contentType = request.getHeader().getHeaders().get("Accept").split(",")[0];
        }

        // baseController
        if (request.getHeader().getHttpMethod() == GET
                && request.getHeader().getUrl().equals("/")) {
            return new BaseResponseDto(StatusCode.OK, baseController.hello(), contentType);
        }

        // userController
        if (request.getHeader().getHttpMethod() == POST
                && request.getHeader().getUrl().equals("/user/create")) {
            Map<String, String> requestBody = extractBody(request.getBody());
            return new BaseResponseDto(StatusCode.FOUND,
                    userController.createUser(requestBody), contentType);
        }

        return new BaseResponseDto(StatusCode.NOT_FOUND, "", contentType);
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
