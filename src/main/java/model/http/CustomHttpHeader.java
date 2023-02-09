package model.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomHttpHeader extends CustomBaseHttpRequest {

    private static final String COOKIE_HEADER = "Cookie";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String ATTRIBUTE_DELIMITER = "; ";
    private static final String KEY_VALUE_DELIMITER = "=";
    private final Map<String, String> header;

    public CustomHttpHeader() {
        this(new HashMap<>());
    }

    public CustomHttpHeader(Map<String, String> header) {
        this.header = header;
    }

    @Override
    public String get(String key) {
        return header.getOrDefault(key, null);
    }

    @Override
    public void put(String key, String value) {
        header.put(key, value);
    }

    public String getOrDefault(String key, String value) {
        return header.getOrDefault(key, value);
    }

    public void respond(DataOutputStream dos) throws IOException {
        for (Map.Entry<String, String> entry : header.entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
        }
    }

    public boolean isLogined() {
        if (!header.containsKey(COOKIE_HEADER)) {
            return false;
        }
        Map<String, String> cookie = Arrays.stream(header.get(COOKIE_HEADER).split(ATTRIBUTE_DELIMITER))
                .map(attribute -> attribute.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(a -> a[0], a-> a[1]));
        if (!cookie.containsKey(JSESSIONID)) {
            return false;
        }
        String id = cookie.get(JSESSIONID);
        return SessionManager.getInstance().findSession(id).isPresent();
    }

}
