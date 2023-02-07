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
    private final FileController fileController;
    private final UserController userController;
    private final RootController rootController;

    public DispatcherServlet() {
        this.fileController = new FileController();
        this.userController = new UserController(new UserService());
        this.rootController = new RootController();
    }

    public void dispatch(HttpRequest request, HttpResponse response) {
        Controller controller = chooseHandler(request.getUri());
        controller.process(request, response);

        if (Objects.isNull(response.getHttpStatus())) {
            throw new MethodNotAllowedException();
        }
    }

    public Controller chooseHandler(String uri) {
        if (uri.endsWith(".html") || uri.endsWith(".css")) {
            return fileController;
        }
        else if (uri.startsWith("/user")) {
            return userController;
        }
        else if (uri.equals("/")) {
            return rootController;
        }
        else {
            throw new PathNotFoundException();
        }
    }
}
