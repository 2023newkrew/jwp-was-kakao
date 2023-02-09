package webserver.request;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Cookie {
    private final Map<String, String> values;

    // 문자열 상수
    private static final String COOKIE_STRING_CONNECTOR = ";|\\s"; // (;, whitespace)를 기준으로 문자열 추출
    private static final String COOKIE_SEPARATOR = "=";

    public static Cookie of(String cookieString) {
        Map<String, String> values = extractTokenValues(cookieString);
        return new Cookie(values);
    }

    private static Map<String, String> extractTokenValues(String cookieString) {
        return Arrays.stream(cookieString.split(COOKIE_STRING_CONNECTOR))
                .map(s -> s.split(COOKIE_SEPARATOR))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1], (a, b) -> b));
    }

    public static Cookie empty() {
        return new Cookie(new HashMap<>());
    }

    public Optional<String> getAttribute(String key) {
        if (!values.containsKey(key)) {
            return Optional.empty();
        }
        return Optional.of(values.get(key));
    }
}
