package infra;

import java.util.HashMap;
import java.util.Map;

public class UriMap {
    private Map<String, Controller> uriMap;

    public UriMap() {
        this.uriMap = new HashMap<>();
    }

    public void put(String uri, Controller controller) {
        this.uriMap.put(uri, controller);
    }

    public Controller controller(String uri) {
        for (Map.Entry<String, Controller> entry : uriMap.entrySet()) {
            if (uri.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
