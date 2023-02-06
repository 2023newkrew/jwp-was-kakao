package http;

import java.util.HashMap;
import java.util.Map;

public class Headers {
    private Map<String, String> headers;

    public Headers () {
        this.headers = new HashMap<>();
    }

    public void put(String str) {
        String[] split = str.split(": ");
        headers.put(split[0], split[1]);
    }

}
