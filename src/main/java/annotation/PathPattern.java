package annotation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class PathPattern {
    private final RequestMethod method;
    private final String path;

    public static PathPattern from(Mapping mapping) {
        return new PathPattern(mapping.method(), mapping.path());
    }

    public static PathPattern of(RequestMethod method, String path) {
        return new PathPattern(method, path);
    }
}
