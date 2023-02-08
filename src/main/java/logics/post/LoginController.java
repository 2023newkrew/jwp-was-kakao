package logics.post;

import logics.Controller;
import logics.Service;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;
import utils.session.Session;
import utils.session.SessionManager;

public class LoginController extends Controller {
    private final Service service = new Service();
    private static final HttpResponse loginSuccess = new HttpResponseVersion1().setResponseCode(302)
            .setHeader("Location", "/index.html");
    private static final HttpResponse loginFail = new HttpResponseVersion1().setResponseCode(302)
            .setHeader("Location", "/user/login_failed.html");
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        try {
            Session session = service.login(httpRequest.getBody());
            SessionManager.getManager.add(session);
            return loginSuccess.setHeader("Set-Cookie", session.toCookieHeader());
        } catch(IllegalArgumentException e){
            return loginFail;
        }
    }
}
