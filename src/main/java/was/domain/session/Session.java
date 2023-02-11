package was.domain.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Session {
    @Getter
    private final String id;
    private final Map<String, Object> values = new HashMap<>();

    public Object getAttribute(String name) {
        return values.getOrDefault(name, null);
    }

    public void setAttribute(String name, Object object) {
        values.put(name, object);
    }
}
