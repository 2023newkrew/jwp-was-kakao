package http.cookie;

import datastructure.StringPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestParsingUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpCookie {
    private static final Logger logger = LoggerFactory.getLogger(HttpCookie.class);
    private final Map<String, String> data;

    public HttpCookie(Map<String, String> data) {
        this.data = Map.copyOf(data);
    }

    public static HttpCookie fromQueryString(String rawCookie) {
        return new HttpCookie(
                RequestParsingUtils.parseCookie(rawCookie)
        );
    }

    public static HttpCookie of(StringPair... pairs) {
        return new HttpCookie(
                Arrays.stream(pairs)
                        .collect(Collectors.toMap(
                                StringPair::getKey,
                                StringPair::getValue
                        ))
        );
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(data.getOrDefault(key, null));
    }

    public String toSetCookieValue() {
        return "JSESSIONID=" + data.get("JSESSIONID") + "; " +
                "Path=" + data.get("Path");
    }

    @Override
    public String toString() {
        return "HttpCookie{" +
                "data=" + data +
                '}';
    }
}
