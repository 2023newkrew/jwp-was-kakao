package was.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {
    public static List<String> parseCookies(String cookie) {
        return Arrays.stream(cookie.split("; "))
                .map(it -> it.substring(11))
                .collect(Collectors.toList());
    }
}
