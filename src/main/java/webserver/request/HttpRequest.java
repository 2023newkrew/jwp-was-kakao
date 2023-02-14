package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import lombok.Getter;
import utils.HttpRequestUtils;
import webserver.session.HttpCookie;
import webserver.session.HttpSession;
import webserver.session.HttpSessionManager;

@Getter
public class HttpRequest {

    private final HttpMethod method;

    private final String path;

    private final Map<String, String> headers;

    private final String body;
    private final HttpSession httpSession;

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        String firstLine = bufferedReader.readLine();
        this.method = HttpRequestUtils.getHttpMethodFromRequestLine(firstLine);
        this.path = HttpRequestUtils.getPathFromRequestLine(firstLine);
        this.headers = HttpRequestUtils.parseHttpHeaders(bufferedReader);
        this.body = HttpRequestUtils.parseHttpBody(bufferedReader, headers);
        this.httpSession = setSession();
    }

    public String getHeaders(String headerKey) {
        return headers.get(headerKey);
    }

    public boolean isRequestedSessionId() {
        return headers.containsKey("Cookie");
    }

    private HttpSession setSession() {
        HttpCookie httpCookie = new HttpCookie(getHeaders("Cookie"));
        String sessionId = httpCookie.getCookie(HttpSession.SESSION_ID_NAME);
        if (sessionId != null) {
            return HttpSessionManager.findHttpSession(sessionId);
        }
        return HttpSessionManager.createSession();
    }
}
