package http.request;

import java.util.Optional;

public abstract class RequestData {
    public abstract Optional<String> get(String key);
}
