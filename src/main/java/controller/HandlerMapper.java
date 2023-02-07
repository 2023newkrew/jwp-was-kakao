package controller;

import request.RequestParams;
import model.User;
import service.UserService;
import supports.StringParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static response.ResponseBody.responseBody;
import static response.ResponseHeader.response200Header;
import static response.ResponseHeader.response302Header;

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
            GetHandler getHandler = new GetHandler(br);
            String requestUrl = getHandler.getRequestUrl(request, requestInfo);
            body = getHandler.getBody(requestUrl);

            response200Header(dos, body.length, stringParser.getUrlType(requestUrl));
            responseBody(dos, body);
        }
        if (request.getMethod().equals("POST")){
            PostHandler postHandler = new PostHandler(br, stringParser);
            String requestUrl = postHandler.getRequestUrl(br);
            String requestBody = postHandler.getRequestBody(requestInfo);
            User user = stringParser.getUserInfo(requestBody);

            UserService userService = new UserService();
            userService.saveUser(user);

            response302Header(dos, requestUrl);
        }
    }

}
