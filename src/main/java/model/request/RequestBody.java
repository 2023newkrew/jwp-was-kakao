package model.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestBody {
    private Map requestBody = new HashMap<>();

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
