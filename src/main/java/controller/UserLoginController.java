package controller;

import db.Database;
import exception.WasException;
import model.User;
import type.HttpStatusCode;
import utils.IOUtils;
import webserver.HttpRequest;
import webserver.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static exception.ErrorCode.CAN_NOT_READ_DATA;
import static exception.ErrorCode.USER_NOT_EXIST;

public class UserLoginController extends Controller {

    private final Database db;

    public UserLoginController(Database db) {
        this.db = db;
    }

    @Override
    protected void doPost(HttpRequest request, DataOutputStream dos) {
        super.doPost(request, dos);
        String requestBody = request.getRequestBody();
        Map<String, String> params = IOUtils.extractParams(requestBody);

        String userId = params.get("userId");
        String password = params.get("password");

        User user = Optional.ofNullable(db.findUserById(userId))
                .orElseThrow(() -> new WasException(USER_NOT_EXIST));

        try {
            if (user.checkPassword(password)) {
                dos.writeBytes(ResponseHeader.of(HttpStatusCode.REDIRECT, "/index.html").getValue());
                return;
            }
            dos.writeBytes(ResponseHeader.of(HttpStatusCode.REDIRECT, "/login_failed.html").getValue());
        } catch (IOException e) {
            // TODO: 적절한 에러코드로 변경 필요
            throw new WasException(CAN_NOT_READ_DATA);
        }
    }
}
