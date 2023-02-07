package webserver.handler;

import static webserver.request.HttpRequestLine.parseQueryParameters;

import db.DataBase;
import java.util.Map;
import model.User;
import webserver.FilenameExtension;
import webserver.request.HttpRequest;
import webserver.HttpResponse;

public class CreateUserHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        Map<String, String> queryParameters = request.getQueryParameters();
        if (queryParameters.isEmpty()) {
            queryParameters = parseQueryParameters(request.getBody());
        }
        DataBase.addUser(
                new User(queryParameters.get("userId"),
                        queryParameters.get("password"),
                        queryParameters.get("name"),
                        queryParameters.get("email"))
        );
        return HttpResponse.found(new byte[0], FilenameExtension.from(""), "/index.html");
    }
}
