package controller;

import model.User;
import request.RequestParams;
import utils.StringParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

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
            String responseUrl = getHomeUrl();
            String requestBody = requestPostBody(requestInfo, stringParser, br);
            postControllerMapping(request, requestBody, responseUrl);
        }
    }

    private void postControllerMapping(RequestParams request, String requestBody, String responseUrl){
        if (request.getUrl().equals("/user/create")){
            User user = stringParser.getUserInfo(requestBody);
            UserController userController = new UserController(dos, responseUrl);
            userController.saveUser(user);
        }
    }


    private void getControllerMapping(RequestParams request, byte[] body){
        response200Header(dos, body.length, request.getContentType());
        responseBody(dos, body);
    }
}
