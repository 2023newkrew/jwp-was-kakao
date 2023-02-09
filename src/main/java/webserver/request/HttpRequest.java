package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import utils.IOUtils;
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
        this.method = HttpMethod.valueOf(firstLine.split(" ")[0]);
        this.path = firstLine.split(" ")[1];
        this.headers = parseHttpHeaders(bufferedReader);
        this.body = parseHttpBody(bufferedReader);
        this.httpSession = setSession();
    }

    public String getHeaders(String headerKey) {
        if (!headers.containsKey(headerKey)) {
            return null;
        }
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

    private String parseHttpBody(BufferedReader bufferedReader) throws IOException {
        if (!this.headers.containsKey("Content-Length")) {
            return "";
        }

        int contentLength = Integer.parseInt(this.headers.get("Content-Length"));
        return IOUtils.readData(bufferedReader, contentLength);
    }

    private Map<String, String> parseHttpHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> header = new HashMap<>();
        String line = bufferedReader.readLine();
        while (!"".equals(line)) {
            int index = line.indexOf(":");
            header.put(line.substring(0, index), line.substring(index + 1)
                    .trim());
            line = bufferedReader.readLine();
        }

        return header;
    }
}
