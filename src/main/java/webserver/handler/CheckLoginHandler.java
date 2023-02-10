package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.session.SessionManager;

public class CheckLoginHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        String sessionId = request.getSessionId();
        if (sessionId == null || SessionManager.findSession(sessionId) == null) {
            return HttpResponse.found("/user/login.html");
        }
        return HttpResponse.found("/index.html");
    }
}
