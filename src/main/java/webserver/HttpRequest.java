package webserver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class HttpRequest {
    private String httpMethod;
    private String url;
    private String protocol;
    private Map<String, String> headers;
}
