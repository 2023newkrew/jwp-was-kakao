package webserver.filter;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.cookie.HttpCookie;
import webserver.http.session.Session;
import webserver.http.session.SessionManager;

import java.util.Objects;
import java.util.UUID;

public class SessionFilter implements Filter {
    @Override
    public void doFilter(final HttpRequest request, final HttpResponse response, final FilterChain chain) {
        System.out.println("Session Filter 호출 ///");
        HttpCookie sessionCookie = request.getCookie("JSESSIONID");
        /* sessionid 생성 */
        if (Objects.isNull(sessionCookie)) {
            UUID uuid = UUID.randomUUID();
            response.setHeader("Set-Cookie", "JSESSIONID=" + uuid + "; Path=/");
            SessionManager.add(new Session(uuid.toString()));
        }
        chain.doFilter(request, response);
    }
}
