package model.request;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static constant.HeaderConstant.*;

public class RequestBody {
    private final Map<String, String> requestBody;

    public Map<String, String> getRequestBody() {
        return Collections.unmodifiableMap(requestBody);
    }

    private RequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
    }

    public static RequestBody of(Map<String, String> requestHeaders, BufferedReader bufferedReader) {
        if (requestHeaders.containsKey(CONTENT_LENGTH)) {
            return new RequestBody(
                    RequestBodyExtractor.extract(requestHeaders, bufferedReader)
            );
        }
        return new RequestBody(new HashMap<>());
    }
}
