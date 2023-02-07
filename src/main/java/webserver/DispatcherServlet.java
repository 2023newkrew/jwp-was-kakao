package webserver;

import common.HttpRequest;
import common.HttpResponse;
import controller.Controller;
import controller.FileController;
import controller.RootController;
import controller.UserController;
import service.UserService;
import support.MethodNotAllowedException;
import support.PathNotFoundException;

import java.util.Objects;

public class DispatcherServlet {
    public static void dispatch(HttpRequest request, HttpResponse response) {
        Controller controller = chooseHandler(request.getUri());
        controller.process(request, response);

        if (Objects.isNull(response.getHttpStatus())) {
            throw new MethodNotAllowedException();
        }
    }

    public static Controller chooseHandler(String uri) {
        if (uri.endsWith(".html") || uri.endsWith(".css")) {
            return new FileController();
        }
        else if (uri.startsWith("/user")) {
            return new UserController(new UserService());
        }
        else if (uri.equals("/")) {
            return new RootController();
        }
        else {
            throw new PathNotFoundException();
        }
    }
}
