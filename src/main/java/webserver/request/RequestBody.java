package webserver.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestBody {

    private Map<String, String> bodyMap = new HashMap<>();

    public RequestBody(String bodyString) {
        bodyString = URLDecoder.decode(bodyString, StandardCharsets.UTF_8);
        setBodyMap(bodyString);
    }

    private void setBodyMap(String bodyString) {
        String[] keyValueTokens = bodyString.split("&");
        for(String keyValueToken : keyValueTokens) {
            String[] keyValue = keyValueToken.split("=");
            if (keyValue.length < 2) continue;
            bodyMap.put(keyValue[0], keyValue[1]);
        }
    }

    public String getBodyValue(String key) {
        return bodyMap.get(key);
    }

    public boolean isEmpty() {
        return bodyMap.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(String key : bodyMap.keySet()) {
            sb.append(key).append(" : ").append(bodyMap.get(key)).append(", ");
        }
        if(sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.append("}").toString();
    }
}
