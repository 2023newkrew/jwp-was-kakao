package model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
public class HttpResponse {
    private final ResponseHeader header;

    public String getBodyValue(String key) {
        return header.getHeaders().get(key);
    }

    public void setAttribute(String key, String value) {
        header.put(key, value);
    }

    public Set<Map.Entry<String, String>> getHeaderEntrySet() {
        return header.getEntrySet();
    }
}
