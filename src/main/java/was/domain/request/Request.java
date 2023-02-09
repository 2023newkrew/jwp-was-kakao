package was.domain.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import was.annotation.RequestMethod;
import was.domain.PathPattern;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@Builder
public class Request {
    private final RequestMethod method;
    private final String path;
    private final Map<String, String> params;
    private final Map<String, String> headers;
    private final String body;

    public PathPattern toPathPattern() {
        return PathPattern.of(method, path);
    }
}
