package webserver.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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
        Map<String, String> values = new HashMap<>();
        String[] split = cookieString.split(COOKIE_STRING_CONNECTOR);
        Arrays.stream(split).forEach(value -> {
            String[] pair = value.split(COOKIE_SEPARATOR);
            values.put(pair[0], pair[1]);
        });
        return values;
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
