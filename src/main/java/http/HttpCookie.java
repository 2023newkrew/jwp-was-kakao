package http;

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

    public static HttpCookie fromQueryString(String queryString) {
        return new HttpCookie(
                RequestParsingUtils.parseQueryString(queryString)
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
        logger.debug("Cookie data : {}", data.entrySet());
        return data.entrySet().stream()
                .map(entry -> String.join("=", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("; "));
    }
}
