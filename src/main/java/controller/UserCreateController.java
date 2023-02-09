package controller;

import db.Database;
import model.User;
import type.HttpStatusCode;
import utils.IOUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ResponseHeader;

import java.io.DataOutputStream;
import java.util.Map;

/**
 * 관련 URI: /user/create
 * 유저 생성 후 index.html로 리다이렉트
 */
public class UserCreateController extends Controller {

    private final Database db;

    public UserCreateController(Database db) {
        this.db = db;
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        db.addUser(new User(
                request.getParam("userId"),
                request.getParam("password"),
                request.getParam("name"),
                request.getParam("email")
        ));
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        Map<String, String> createUserReqMap = IOUtils.extractParams(request.getRequestBody());
        db.addUser(new User(
                createUserReqMap.get("userId"),
                createUserReqMap.get("password"),
                createUserReqMap.get("name"),
                createUserReqMap.get("email")
        ));
    }

    @Override
    public void doFinally(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        response.setResponseHeader(ResponseHeader.of(HttpStatusCode.REDIRECT, "/index.html"));
    }

}
