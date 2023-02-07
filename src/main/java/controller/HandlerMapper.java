package controller;

import model.User;
import request.RequestParams;
import service.UserService;
import utils.StringParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static response.ResponseBody.responseBody;
import static response.ResponseHeader.response200Header;
import static utils.FileIoUtils.*;

public class HandlerMapper {
    private final BufferedReader br;
    private final DataOutputStream dos;
    private final StringParser stringParser;

    public HandlerMapper(BufferedReader br, DataOutputStream dos, StringParser stringParser) {
        this.br = br;
        this.dos = dos;
        this.stringParser = stringParser;
    }

    public void methodMapping(RequestParams request, String requestInfo) throws IOException, URISyntaxException {
        byte[] body;
        if (request.getMethod().equals("GET")){
            String requestUrl = requestGetUrl(request, requestInfo, br);
            body = readFile(requestUrl);
            getControllerMapping(request, body);
        }
        if (request.getMethod().equals("POST")){
            String requestBody = Objects.requireNonNull(requestPostBody(requestInfo, stringParser, br)).orElseThrow(() ->
                    new NullPointerException("There's no request body"));
            postControllerMapping(request, requestBody);
        }
    }

    private void postControllerMapping(RequestParams request, String requestBody){
        if (request.getUrl().equals("/user/create")){
            String responseUrl = getIndexUrl();
            User user = stringParser.getUserInfo(requestBody);
            UserController userController = new UserController(dos, new UserService());
            userController.saveUser(user, responseUrl);
        }
    }

    private void getControllerMapping(RequestParams request, byte[] body){
        response200Header(dos, body.length, request.getContentType());
        responseBody(dos, body);
    }
}
