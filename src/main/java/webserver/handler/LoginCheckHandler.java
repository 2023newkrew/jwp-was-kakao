package webserver.handler;

import model.User;
import webserver.common.HttpSession;
import webserver.common.HttpSessions;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Objects;

public class LoginCheckHandler implements Handler {
    @Override
    public Response apply(Request request) {
        String sessionId = request.getSessionId();
        if (Objects.isNull(sessionId) || isUserNotSaved(sessionId)) {
            return Response.found(
                new byte[0],
                request.findRequestedFileType(),
                "/user/login.html"
            );
        }

        return Response.found(
            new byte[0],
            request.findRequestedFileType(),
            "/index.html"
        );
    }

    private boolean isUserNotSaved(String sessionId) {
        HttpSession httpSession = HttpSessions.get(sessionId);
        if (httpSession == null) {
            return true;
        }

        User loginUser = (User) httpSession.getAttribute("user");
        return loginUser == null;
    }
}
