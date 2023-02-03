package webserver;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class Request {

    private HttpMethod httpMethod;

    private String url;

    private String httpVersion;

    private Map<String, String> headers;

    private String body;
}
