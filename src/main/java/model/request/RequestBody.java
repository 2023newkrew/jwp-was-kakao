package model.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
public class RequestBody {
    private final Map requestBody;
    public Map getRequestBody() {
        return Collections.unmodifiableMap(requestBody);
    }

    private RequestBody(Map requestBody) {
        this.requestBody = requestBody;
    }

    public static RequestBody of(Map map) {
        return new RequestBody(map);
    }
}
