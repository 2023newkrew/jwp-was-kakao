package webserver.handler;

import webserver.common.HttpSessions;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Objects;

public class LoginCheckHandler implements Handler {
    @Override
    public Response apply(Request request) {
        String sessionId = request.getSessionId();
        if (
            Objects.isNull(sessionId) ||
            Objects.isNull(HttpSessions.findValue(sessionId, "user"))
        ) {
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
}
