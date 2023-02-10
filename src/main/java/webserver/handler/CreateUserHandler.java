package webserver.handler;

import static webserver.request.HttpRequestLine.parseQueryParams;

import db.DataBase;
import java.util.Map;
import model.User;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class CreateUserHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        Map<String, String> queryParams = request.getQueryParams();
        if (queryParams.isEmpty()) {
            queryParams = parseQueryParams(request.getBody());
        }
        DataBase.addUser(
                new User(queryParams.get("userId"),
                        queryParams.get("password"),
                        queryParams.get("name"),
                        queryParams.get("email"))
        );
        return HttpResponse.found("/index.html");
    }
}
