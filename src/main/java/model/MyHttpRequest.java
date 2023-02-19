package model;

import db.SessionStorage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpMethod;

public class MyHttpRequest {

    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private final Map<String, String> body;
    private final Map<String, String> cookies;
    public static final String JSESSIONID = "JSESSIONID";

    public MyHttpRequest(String httpMethod, String url, Map<String, String> queryParams, Map<String, String> headers,
            Map<String, String> cookies, Map<String, String> body) {
        this(HttpMethod.resolve(httpMethod), url, queryParams, headers, cookies, body);
    }

    public MyHttpRequest(HttpMethod httpMethod, String url, Map<String, String> queryParams,
            Map<String, String> headers, Map<String, String> cookies, Map<String, String> body) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.queryParams = new HashMap<>(queryParams);
        this.headers = new HashMap<>(headers);
        this.cookies = new HashMap<>(cookies);
        this.body = new HashMap<>(body);
    }

    public boolean isStaticRequest() {
        return Arrays.stream(StaticFile.values())
                .anyMatch(fileName -> this.url.startsWith(fileName.getValue(), 1));
    }

    public String getContentType() {
        return this.headers.getOrDefault("Accept", "text/html,").split(",")[0];
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, String> getBody() {
        return new HashMap<>(body);
    }

    public boolean hasSession() {
        return this.cookies.containsKey(JSESSIONID);
    }

    public String getSession() {
        return this.cookies.get(JSESSIONID);
    }

    public String createSession() {
        String sessionId = UUID.randomUUID().toString();
        this.cookies.put(JSESSIONID, sessionId);
        SessionStorage.add(sessionId, new Session(sessionId));
        return sessionId;
    }
}
