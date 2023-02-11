package was.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Cookie {
    private final String uuid;
    private final String path;
}
