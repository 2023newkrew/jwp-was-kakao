package webserver.controller;

import java.util.Arrays;
import webserver.request.HttpMethod;
import webserver.request.HttpRequest;

public enum ControllerMapping {
    BASE(HttpMethod.GET, "/", new DefaultController()),
    CREATE_USER(HttpMethod.POST, "/user/create", new UserRegisterController()),
    USER_LOGIN(HttpMethod.POST, "/user/login", new UserLoginController()),
    USER_LIST(HttpMethod.GET, "/user/list", new UserListController()),
    FILE_LOAD(HttpMethod.GET, "", new FileLoadController()),
    USER_LOGIN_PAGE(HttpMethod.GET, "/user/login.html", new UserLoginPageController());

    private final HttpMethod httpMethod;
    private final String path;
    private final Controller controller;

    ControllerMapping(HttpMethod httpMethod, String path, Controller controller) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.controller = controller;
    }

    public static Controller findByHttpRequest(HttpRequest httpRequest) {
        HttpMethod httpMethod = httpRequest.getMethod();
        String path = httpRequest.getPath();
        return Arrays.stream(values())
                .filter(controllerMapping -> controllerMapping.httpMethod == httpMethod && controllerMapping.path.equals(path))
                .findAny()
                .orElse(FILE_LOAD).controller;
    }
}
