package controller;

import db.DataBase;
import model.User;
import type.HttpStatusCode;
import webserver.HttpRequest;
import webserver.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 관련 URI: /user/create
 * 유저 생성 후 index.html로 리다이렉트
 */
public class UserCreateController extends Controller {

    @Override
    public void process(HttpRequest request, DataOutputStream dos) throws IOException {
        DataBase.addUser(new User(
                request.getParam("userId"),
                request.getParam("password"),
                request.getParam("name"),
                request.getParam("email")
        ));
        // index로 redirect
        dos.writeBytes(ResponseHeader.of(HttpStatusCode.REDIRECT, "/index.html").getValue());
        responseBody(dos, new byte[0]);
    }

}
