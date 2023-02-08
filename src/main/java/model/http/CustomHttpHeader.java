package model.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomHttpHeader extends CustomBaseHttpRequest {

    private Map<String, String> header;

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

    public void respond(DataOutputStream dos) throws IOException {
        for (Map.Entry<String, String> entry : header.entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + " \r\n");
        }
    }

}
