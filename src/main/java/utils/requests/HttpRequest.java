package utils.requests;

import java.net.URI;

public interface HttpRequest {
    URI getURI();
    String getHttpVersion();
    RequestMethod getRequestMethod();
    String getBody();
    String getHeaderParameter (String s);
}
