package webserver;

import java.time.LocalDateTime;

public class SessionManager {

    private final SessionStorage sessionStorage;

    public SessionManager(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    /**
     * 쿠키에 JSESSIONID가 담겨있다면 해당 정보를 바탕으로 세션 조회 후 리턴
     * ID가 없거나 해당 ID와 관련한 세션이 없는 경우 새로운 세션 시작
     */
    public HttpSession getSession(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpCookie cookie = new HttpCookie(httpRequest.getRequestHeader().get("Cookie").orElse(""));

        HttpSession session = sessionStorage.getSession(cookie.get("JSESSIONID").orElse(""));
        if (session == null || LocalDateTime.now().isAfter(session.getExpireDateTime())) {
            HttpSession newSession = sessionStorage.createHttpSession();
            httpResponse.getResponseHeader().putCookieItem("JSESSIONID", newSession.getId());
            return newSession;
        }

        return session;
    }

}

