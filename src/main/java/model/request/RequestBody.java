package model.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
public class RequestBody {
    private final Map<String, String> requestBody;

    public Map<String, String> getRequestBody() {
        return Collections.unmodifiableMap(requestBody);
    }

    private RequestBody(HashMap<String, String> requestBody) {
        this.requestBody = requestBody;
    }

    public static RequestBody of(Map map) {
        return new RequestBody((HashMap<String, String>) map);
    }
}
