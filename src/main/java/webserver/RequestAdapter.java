package webserver;

import controller.RequestController;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import model.Request;
import model.Response;
import org.springframework.http.HttpMethod;

public class RequestAdapter {
    private final RequestController requestController = new RequestController();

    public void mapHandler(Request request, Response response) throws URISyntaxException, IOException {

        HttpMethod currentMethod = request.getMethod();

        String currentPath = request.getPath();

        // GET
        if (currentMethod.equals(HttpMethod.GET)) {

            // Static, favicon

            if (request.getRoot().equals("./static") || currentPath.equals("/favicon.ico")) {
                requestController.sendPage(request, response);
                return;
            }

            // Main

            if (currentPath.equals("/") || currentPath.equals("/index") || currentPath.equals("/index.html")) {
                requestController.sendMainPage(request, response);
                return;
            }

            // User

            if (currentPath.equals("/user/login.html") || currentPath.equals("/user/login")) {
                if (isLogin(request, response)) {
                    redirectToMainPage(response);
                    return;
                };
                requestController.sendLoginPage(request, response);
                return;
            }

            if (currentPath.equals("/user/login_failed.html") || currentPath.equals("/user/login_failed")) {
                if (isLogin(request, response)) {
                    redirectToMainPage(response);
                    return;
                };
                requestController.sendLoginFailedPage(request, response);
                return;
            }

            if (currentPath.equals("/user/form.html") || currentPath.equals("/user/form")) {
                if (isLogin(request, response)) {
                    redirectToMainPage(response);
                    return;
                };
                requestController.sendSignupPage(request, response);
                return;
            }

            if (currentPath.equals("/user/profile.html") || currentPath.equals("/user/profile")) {
                if (!isLogin(request, response)) {
                    redirectToLoginPage(response);
                };
                requestController.sendProfilePage(request, response);
                return;
            }

            if (currentPath.equals("/user/list.html") || currentPath.equals("/user/list")) {
                if (!isLogin(request, response)) {
                    redirectToLoginPage(response);
                }
                requestController.sendUserListPage(request, response);
                return;
            }

            // Qna

            if (currentPath.equals("/qna/show.html") || currentPath.equals("/qna/show")) {
                requestController.sendQnaShowPage(request, response);
                return;
            }

            if (currentPath.equals("/qna/form.html") || currentPath.equals("/qna/form")) {
                if (!isLogin(request, response)) {
                    redirectToLoginPage(response);
                }
                requestController.sendQnaFormPage(request, response);
                return;
            }
        }

        // POST
        if (currentMethod.equals(HttpMethod.POST)) {

            // User

            if (currentPath.equals("/user/create")) {
                requestController.createUser(request, response);
                return;
            }

            if (currentPath.equals("/user/login")) {
                if (isLogin(request, response)) {
                    redirectToMainPage(response);
                    return;
                };
                requestController.login(request, response);
            }
        }
    }

    private boolean isLogin(Request request, Response response) {
        return Objects.nonNull(request.getSession().getAttributes("user"));
    }

    private void redirectToMainPage(Response response) throws IOException, URISyntaxException {
        response.found(new URI("/index.html"));
        response.send();
    }

    private void redirectToLoginPage(Response response) throws IOException, URISyntaxException {
        response.found(new URI("/user/login.html"));
        response.send();
    }
}
