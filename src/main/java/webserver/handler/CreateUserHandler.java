package webserver.handler;

import static webserver.request.HttpRequestLine.parseQueryParameters;

import db.DataBase;
import java.util.Map;
import model.User;
import org.springframework.http.HttpStatus;
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

        HttpResponse response = new HttpResponse(HttpStatus.FOUND);
        response.addHeader("Location", "/index.html");
        return response;

    }
}
