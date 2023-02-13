package was.domain.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import was.annotation.RequestMethod;
import was.domain.Cookie;
import was.domain.PathPattern;

import java.util.Map;

@RequiredArgsConstructor
@Builder
@Getter
public class Request {
    private final RequestMethod method;
    private final String path;
    private final Map<String, String> params;
    private final String body;
    private final Cookie cookie;

    public PathPattern toPathPattern() {
        return PathPattern.of(method, path);
    }
}
