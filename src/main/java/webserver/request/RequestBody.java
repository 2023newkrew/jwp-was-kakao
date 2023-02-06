package webserver.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestBody {
    private static final String FORM = "application/x-www-form-urlencoded";
    private Map<String, String> formData = new HashMap<>();

    public RequestBody(String bodyString, String contentType) {
        bodyString = URLDecoder.decode(bodyString, StandardCharsets.UTF_8);
        if(FORM.equals(contentType)) {
            setFormData(bodyString);
        }
    }

    private void setFormData(String bodyString) {
        String[] keyValueTokens = bodyString.split("&");
        for(String keyValueToken : keyValueTokens) {
            String[] keyValue = keyValueToken.split("=");
            if (keyValue.length < 2) continue;
            formData.put(keyValue[0], keyValue[1]);
        }
    }

    public String getFormData(String key) {
        return formData.get(key);
    }

    public boolean isEmpty() {
        return formData.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(String key : formData.keySet()) {
            sb.append(key).append(" : ").append(formData.get(key)).append(", ");
        }
        if(sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.append("}").toString();
    }
}
