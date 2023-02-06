package web.controller;

import utils.ParameterUtils;
import web.domain.MemoryUserRepository;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import web.domain.User;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

public class PostSignInController implements Controller {

    private static final String PATH = "/user/create";
    private static final String INDEX_PAGE_PATH = "/index.html";

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        Map<String, String> params = ParameterUtils.parse(httpRequest.getBody());
        MemoryUserRepository.addUser(new User(
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
