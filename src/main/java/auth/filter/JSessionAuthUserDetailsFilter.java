package auth.filter;

import auth.AuthUserDetails;
import infra.filter.MyFilter;
import infra.session.SessionManager;
import infra.utils.request.MyRequest;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.HttpHeaders;

public class JSessionAuthUserDetailsFilter implements MyFilter {

    public static final String JSESSIONID = "JSESSIONID";
    private final SessionManager sessionManager;

    public JSessionAuthUserDetailsFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void accept(MyRequest myRequest) {
        if (Objects.nonNull(myRequest.getHeader(HttpHeaders.COOKIE))) {
            extractAuthUserDetails(myRequest);
        }
    }

    private void extractAuthUserDetails(MyRequest myRequest) {
        AuthUserDetails details = findLoginUser(myRequest.getHeader(HttpHeaders.COOKIE));
        if (Objects.nonNull(details)) {
            myRequest.putContext(AuthUserDetails.class, details);
        }
    }

    private AuthUserDetails findLoginUser(String data) {
        String[] cookies = data.split("; ");
        String jSessionKeyAndValue = findSessionCookie(cookies);
        if (Objects.nonNull(jSessionKeyAndValue)) {
            String key = jSessionKeyAndValue.split("=")[1];
            return (AuthUserDetails) sessionManager.findSession(key).getAttribute(key);
        }
        return null;
    }

    private String findSessionCookie(String[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie ->
                        cookie.startsWith(JSESSIONID))
                .filter(jSessionCookie -> {
                    String value = jSessionCookie.split("=")[1];
                    return Objects.nonNull(sessionManager.findSession(value));
                })
                .findFirst().orElse(null);
    }

}
