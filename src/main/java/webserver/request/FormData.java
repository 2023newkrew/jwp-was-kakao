package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class FormData {

    private final Map<String, String> form;

    public FormData(String body) {
            Map<String, String> form = new HashMap<>();
            String[] keyValueTokens = body.split("&");
            for(String keyValueToken : keyValueTokens) {
                String[] keyValue = keyValueToken.split("=");
                if (keyValue.length < 2) continue;
                form.put(keyValue[0], keyValue[1]);
            }
            this.form = form;
    }

    public String getValue(String key) {
        return form.get(key);
    }
}
