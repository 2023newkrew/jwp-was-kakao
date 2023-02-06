package web.controller;

import db.DataBase;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.User;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostSignInController implements Controller {

    private static final String PATH = "/user/create";
    private static final String INDEX_PAGE_PATH = "/index.html";

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        String body = httpRequest.getBody();
        Map<String, String> params = Arrays.stream(body.split("&"))
                .map(parameter -> parameter.split("="))
                .collect(Collectors.toMap(parameter -> parameter[0], parameter -> parameter[1]));

        DataBase.addUser(new User(
                params.get("userId"),
                params.get("password"),
                decodeParam(params.get("name")),
                decodeParam(params.get("email"))
        ));

        return HttpResponse.redirect(INDEX_PAGE_PATH);
    }

    private String decodeParam(String param) {
        return URLDecoder.decode(param, Charset.defaultCharset());
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.POST, httpRequest.getMethod()) && Objects.equals(PATH, httpRequest.getPath());
    }
}
