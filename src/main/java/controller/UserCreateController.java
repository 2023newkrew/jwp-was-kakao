package controller;

import db.DataBase;
import model.User;
import type.HttpStatusCode;
import utils.IOUtils;
import webserver.HttpRequest;
import webserver.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 관련 URI: /user/create
 * 유저 생성 후 index.html로 리다이렉트
 */
public class UserCreateController extends Controller {

    @Override
    public void doGet(HttpRequest request, DataOutputStream dos) {
        String reqMethod = request.getRequestHeader().get("method").orElseThrow(IllegalArgumentException::new);

        DataBase.addUser(new User(
                request.getParam("userId"),
                request.getParam("password"),
                request.getParam("name"),
                request.getParam("email")
        ));
    }

    @Override
    public void doPost(HttpRequest request, DataOutputStream dos) {
        Map<String, String> createUserReqMap = IOUtils.extractParams(request.getRequestBody());
        DataBase.addUser(new User(
                createUserReqMap.get("userId"),
                createUserReqMap.get("password"),
                createUserReqMap.get("name"),
                createUserReqMap.get("email")
        ));
    }

    @Override
    public void doFinally(HttpRequest request, DataOutputStream dos) {
        try {
            dos.writeBytes(ResponseHeader.of(HttpStatusCode.REDIRECT, "/index.html").getValue());
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        responseBody(dos, new byte[0]);
    }

}
