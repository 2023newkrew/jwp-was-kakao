package logics.get;

import logics.Controller;
import logics.Service;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;
import utils.session.SessionManager;

import java.util.Objects;

/**
 * In case of being already logged-in, redirect to index.html
 */
public class LoginPageController extends Controller {
    private final Service service = new Service();
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        String sessionId = service.parseSessionKey(httpRequest.getHeaderParameter("Cookie"));
        if (Objects.isNull(sessionId) || Objects.isNull(SessionManager.getManager.findSession(sessionId))) {
            return defaultPathHandling(httpRequest);
        }
        return new HttpResponseVersion1().setResponseCode(302)
                .setHeader("Location", "/index.html");
    }

}
