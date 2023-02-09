package webserver.filter;

import static model.MyHttpRequest.JSESSIONID;

import db.SessionStorage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.MyHttpRequest;
import model.MyHttpResponse;
import webserver.MyFilter;
import webserver.MyFilterChain;
import webserver.MyHttpCookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionFilter implements MyFilter {

    private static final SessionFilter INSTANCE = new SessionFilter();

    public static SessionFilter getInstance() {
        return INSTANCE;
    }

    @Override
    public void doChain(MyHttpRequest httpRequest, MyHttpResponse httpResponse, MyFilterChain chain) {
        handleSession(httpRequest, httpResponse);
        chain.doChain(httpRequest, httpResponse);
    }

    private void handleSession(MyHttpRequest httpRequest, MyHttpResponse httpResponse) {
        if (httpRequest.hasSession() && SessionStorage.findSession(httpRequest.getSession()) != null) {
            return;
        }

        String sessionId = httpRequest.createSession();
        httpResponse.setCookie(new MyHttpCookie(JSESSIONID, sessionId));
    }
}
