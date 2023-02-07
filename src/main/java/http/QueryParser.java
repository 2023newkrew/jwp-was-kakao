package http;

import java.util.HashMap;
import java.util.Map;

public class QueryParser {
    public static Map<String, String> parse(String queryString) {
        Map<String, String> map = new HashMap<>();
        if(queryString==null || queryString.isEmpty())
            return map;



        String[] fields = queryString.split("&");
        for (String field : fields) {
            String key = field.split("=")[0];
            String value = field.split("=")[1];
            map.put(key, value);
        }
        return map;
    }
}
