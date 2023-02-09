package logics.controller.get;

import logics.Service.SessionService;
import logics.controller.Controller;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

/**
 *
 * respond when receiving "GET /user/login"
 * In case of being already logged-in, redirect to index.html
 */
public class LoginPageController implements Controller {

    public static Controller instance = new LoginPageController();
    private final SessionService sessionService = SessionService.instance;

    private LoginPageController(){}
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        String sessionId = sessionService.parseSessionKey(httpRequest.getHeaderParameter("Cookie"));
        if (sessionService.isValidSessionKey(sessionId)) {
            return new HttpResponseVersion1().setResponseCode(302)
                    .setHeader("Location", "/index.html");
        }
        return DefaultPathController.instance.makeResponse(httpRequest);
    }

}
