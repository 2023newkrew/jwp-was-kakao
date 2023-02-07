package controller;

import request.RequestParams;
import model.User;
import utils.FileIoUtils;
import utils.IOUtils;
import utils.StringParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static response.ResponseBody.responseBody;
import static response.ResponseHeader.response200Header;

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
            String requestUrl = requestGetUrl(request, requestInfo);
            body = readFile(requestUrl);
            getControllerMapping(request, body);
        }
        if (request.getMethod().equals("POST")){
            String responseUrl = redirectUrl(br);
            String requestBody = requestPostBody(requestInfo);
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
        // always
        response200Header(dos, body.length, request.getContentType());
        responseBody(dos, body);
    }


    private String requestGetUrl(RequestParams request, String s) throws IOException {
        if (request.getUrl().startsWith("/css") || request.getUrl().startsWith("/js")){
            return "./static" + request.getUrl();
        }
        if (request.getUrl().equals("/")){
            return null;
        }
        if (request.getUrl().startsWith("/") || request.getUrl().endsWith("html")){
            return "./templates" + request.getUrl();
        }
        while (!"".equals(s)) {
            s = br.readLine();
        }
        return request.getUrl();
    }

    private byte[] readFile(String requestUrl) throws IOException, URISyntaxException {
        try {
            return FileIoUtils.loadFileFromClasspath(requestUrl);
        } catch (NullPointerException e) {
            return "Hello world".getBytes();
        }
    }

    private String redirectUrl(BufferedReader br) {
        return "/index.html";
    }

    private String requestPostBody(String requestInfo) throws IOException {
        while (stringParser.getContentLength(requestInfo) == -1) {
            requestInfo = br.readLine();
        }
        if (requestInfo.length() == 0) {
            return null;
        }
        return IOUtils.readData(br, stringParser.getContentLength(requestInfo));
    }
}
