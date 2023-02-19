package webserver.handler;

import db.DataBase;
import http.*;
import model.User;
import webserver.http.HttpRequestParamParser;

import java.util.*;

public class UserCreateRequestHandler implements UrlMappingHandler {

    private static final String URL_MAPPING_REGEX = "/user/create";
    private static final HttpRequestParamParser httpRequestParamParser = new HttpRequestParamParser();

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpRequestParams params;
        if (httpRequest.getMethod() == HttpMethod.GET) {
            params = httpRequest.getParameters();
        } else {
            params = httpRequestParamParser.parse(httpRequest.getBody());
        }

        User user = createUser(params);
        DataBase.addUser(user);

        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.addHeader(HttpHeaders.LOCATION, List.of("/index.html"));
    }

    @Override
    public boolean support(HttpRequest httpRequest) {
        return httpRequest.getMethod() == HttpMethod.GET || httpRequest.getMethod() == HttpMethod.POST;
    }

    @Override
    public String getUrlMappingRegex() {
        return URL_MAPPING_REGEX;
    }

    private User createUser(HttpRequestParams params) {
        validateUserParams(params);

        return new User(
                params.getParameter("userId"),
                params.getParameter("password"),
                params.getParameter("name"),
                params.getParameter("email"));
    }

    private void validateUserParams(HttpRequestParams params) {
        if (!params.hasParameter("userId") ||
                !params.hasParameter("password") ||
                !params.hasParameter("name") ||
                !params.hasParameter("email")) {
            throw new IllegalArgumentException();
        }
    }
}
