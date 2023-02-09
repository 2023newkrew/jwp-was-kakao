package controller;

import db.Database;
import exception.WasException;
import model.User;
import type.HttpStatusCode;
import utils.IOUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ResponseHeader;

import java.io.DataOutputStream;
import java.util.Map;
import java.util.Optional;

import static exception.ErrorCode.USER_NOT_EXIST;

public class UserLoginController extends Controller {

    private final Database db;

    public UserLoginController(Database db) {
        this.db = db;
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        super.doPost(request, response, dos);
        String requestBody = request.getRequestBody();
        Map<String, String> params = IOUtils.extractParams(requestBody);

        String userId = params.get("userId");
        String password = params.get("password");

        User user = Optional.ofNullable(db.findUserById(userId))
                .orElseThrow(() -> new WasException(USER_NOT_EXIST));

        if (user.checkPassword(password)) {
            response.setResponseHeader(ResponseHeader.of(HttpStatusCode.REDIRECT, "/index.html"));
            return;
        }
        response.setResponseHeader(ResponseHeader.of(HttpStatusCode.REDIRECT, "/login_failed.html"));
    }
}
