package annotation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class Request {
    private final RequestMethod method;
    private final String path;

    public static Request from(Mapping mapping) {
        return new Request(mapping.method(), mapping.path());
    }
}
