package webserver;

import java.util.Map;

public interface Controller {

    Response handleRequest(String uri, Map<String, String> params);
}
