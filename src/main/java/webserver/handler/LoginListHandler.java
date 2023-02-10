package webserver.handler;

import db.DataBase;
import utils.TemplateEngine;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class LoginListHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        if (request.getHeaders().getCookie().get("JSESSIONID") != null) {
            return TemplateEngine
                    .getTemplateResponse("/user/list", DataBase.findAll());
        }
        return HttpResponse.found("/user/login.html");
    }
}
