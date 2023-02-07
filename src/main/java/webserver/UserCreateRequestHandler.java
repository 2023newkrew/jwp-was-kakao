package webserver;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UserCreateRequestHandler extends PostRequestHandler {

    private UserCreateRequestHandler() {
    }

    private static class UserPostRequestHandlerHolder {
        public static final UserCreateRequestHandler INSTANCE = new UserCreateRequestHandler();
    }

    public static UserCreateRequestHandler getInstance() {
        return UserCreateRequestHandler.UserPostRequestHandlerHolder.INSTANCE;
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        String path = request.getTarget()
                .getPath();
        return super.canHandle(request) &&
                path.equals("/user/create");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String requestBody = request.getBody();
        String requestPath = request.getTarget()
                .getPath();
        requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
        MultiValueMap<String, String> requestParams = UriComponentsBuilder.fromUriString(requestPath)
                .query(requestBody)
                .build()
                .getQueryParams();

        if (requestPath.equals("/user/create")) {
//            addUser(requestParams);
            return new HttpResponse.Builder()
                    .setStatus(HttpStatus.OK)
                    .addHeader("Location", "http://localhost:8080/index.html")
                    .build();
        }
        return null;
    }
}
