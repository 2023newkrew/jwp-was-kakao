package http;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CookieList {

    private static final String COOKIE_DELIMITER = "; ";
    private static final String NAME_VALUE_DELIMITER = "=";

    private final List<Cookie> cookies;

    private CookieList(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public static CookieList empty() {
        return new CookieList(new ArrayList<>());
    }

    public static CookieList parse(String header) {
        if (header == null || header.isBlank()) {
            return CookieList.empty();
        }
        List<Cookie> cookies = new ArrayList<>();
        String[] cookieStrings = header.split(COOKIE_DELIMITER);
        for (String cookieString : cookieStrings) {
            String[] nameValuePair = cookieString.split(NAME_VALUE_DELIMITER);
            cookies.add(new Cookie(nameValuePair[0], nameValuePair[1]));
        }
        return new CookieList(cookies);
    }

    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    public boolean hasCookie(String name) {
        return this.cookies.stream().anyMatch(cookie -> Objects.equals(cookie.getName(), name));
    }

    @Override
    public String toString() {
        return cookies.stream().map(Cookie::toString)
                .collect(Collectors.joining(COOKIE_DELIMITER));
    }
}
