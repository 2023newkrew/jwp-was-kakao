package annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Request {
    private final RequestMethod method;
    private final String path;
    private final Map<String, String> params;
    private final String body;

    public PathPattern toPathPattern() {
        return PathPattern.of(method, path);
    }
}
