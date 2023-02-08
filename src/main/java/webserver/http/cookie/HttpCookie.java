package webserver.http.cookie;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpCookie {
    private final Map<String, String> cookies;

    private HttpCookie(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static HttpCookie from(String cookieValues) {
        Map<String, String> cookies =
                Arrays.stream(cookieValues.split(";"))
                        .map(String::trim)
                        .map(value -> value.split("="))
                        .collect(Collectors.toMap(a -> a[0], a -> a[1]));

        return new HttpCookie(cookies);
    }

    public Optional<String> getCookie(String key) {
        return Optional.ofNullable(cookies.get(key));
    }
}
