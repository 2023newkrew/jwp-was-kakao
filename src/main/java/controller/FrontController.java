package controller;

import exception.NotFoundException;
import exception.RedirectException;
import view.StaticViewRenderer;
import view.TemplateViewRenderer;
import view.ViewRenderer;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;
import webserver.SessionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class FrontController {
    private static final Map<String, Controller> controllerMap = Map.of(
            "POST /user/create", new UserSaveController(),
            "POST /user/login", new LoginController(),
            "GET /user/list", new UserListController(),
            "GET /user/login", new LoginPageController(),
            "GET /", new RootController()
    );

    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws NotFoundException, RedirectException {
        try {
            if (httpRequest.getSession() == null) {
                HttpSession session = SessionManager.createSession();
                httpResponse.setJSessionId(session.getId());
            }

            String url = httpRequest.getUrl();

            String requestSignature = httpRequest.getMethod().name() + " " + url;
            if (controllerMap.containsKey(requestSignature)) {
                Controller controller = controllerMap.get(requestSignature);
                controller.process(httpRequest, httpResponse);
                return;
            }

            ViewRenderer viewRenderer = StaticViewRenderer.isStaticUrl(url) ?
                    new StaticViewRenderer(url) : new TemplateViewRenderer(url);
            viewRenderer.render(httpResponse);
        } catch (FileNotFoundException e) {
            throw new NotFoundException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
