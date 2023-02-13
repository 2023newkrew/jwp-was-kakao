package webserver;

import common.HttpRequest;
import common.HttpResponse;
import controller.Controller;
import controller.FileController;
import controller.RootController;
import controller.UserController;
import service.AuthService;
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
        this.userController = new UserController(new UserService(), new AuthService(new SessionManager()));
        this.rootController = new RootController();
    }

    public void dispatch(HttpRequest request, HttpResponse response) {
        Controller controller = chooseHandler(request.getUri());
        controller.process(request, response);

        // 해당 경로 존재하나 HTTP Method 맞지 않아 처리되지 않았을 경우
        if (Objects.isNull(response.getHttpStatus())) {
            throw new MethodNotAllowedException();
        }
    }

    public Controller chooseHandler(String uri) {
        if (uri.endsWith(".html") || uri.endsWith(".css") || uri.endsWith(".js")) {
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
