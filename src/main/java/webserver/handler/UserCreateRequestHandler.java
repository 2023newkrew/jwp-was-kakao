package webserver.handler;

import db.DataBase;
import http.*;
import model.User;

import java.util.*;

public class UserCreateRequestHandler implements UrlMappingHandler {

    private static final String URL_MAPPING_REGEX = "/user/create";

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        Map<String, String> params;
        if (httpRequest.getMethod() == HttpMethod.GET) {
            params = httpRequest.getParameters();
        } else {
            params = parseBody(httpRequest.getBody());
        }

        User user = createUser(params);
        DataBase.addUser(user);

        return HttpResponse.HttpResponseBuilder.aHttpResponse()
                .withStatus(HttpStatus.FOUND)
                .withVersion("HTTP/1.1")
                .withHeaders(Map.of(HttpHeader.LOCATION, List.of("/index.html")))
                .build();
    }

    private User createUser(Map<String, String> params) {
        validateUserParams(params);

        return new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email"));
    }

    private void validateUserParams(Map<String, String> params) {
        if (!params.containsKey("userId") ||
                !params.containsKey("password") ||
                !params.containsKey("name") ||
                !params.containsKey("email")) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean support(HttpRequest httpRequest) {
        return httpRequest.getMethod() == HttpMethod.GET || httpRequest.getMethod() == HttpMethod.POST;
    }

    @Override
    public String getUrlMappingRegex() {
        return URL_MAPPING_REGEX;
    }
    
    private Map<String, String> parseBody(String body){
        Map<String, String> params = new HashMap<>();
        
        String[] splitParams = body.split("&");
        
        Arrays.stream(splitParams)
                .filter(param -> !param.isBlank())
                .map(param -> param.split("=", 2))
                .map(this::convertToEntry)
                .forEach(paramEntry -> params.put(paramEntry.getKey(), paramEntry.getValue()));

        return params;
    }

    private Map.Entry<String, String> convertToEntry(String[] nameAndValue) {
        if (nameAndValue.length == 1) {
            return new AbstractMap.SimpleEntry<>(nameAndValue[0], "");
        } else {
            return new AbstractMap.SimpleEntry<>(nameAndValue[0], nameAndValue[1]);
        }
    }
}
