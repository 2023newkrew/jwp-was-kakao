package logics.controller.get;

import logics.Service.SessionService;
import logics.controller.Controller;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

/**
 * In case of being already logged-in, redirect to index.html
 */
public class LoginPageController implements Controller {
    private final SessionService sessionService = new SessionService();
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        String sessionId = sessionService.parseSessionKey(httpRequest.getHeaderParameter("Cookie"));
        if (sessionService.isValidSessionKey(sessionId)) {
            return new HttpResponseVersion1().setResponseCode(302)
                    .setHeader("Location", "/index.html");
        }
        return new DefaultPathController().makeResponse(httpRequest);
    }

}
