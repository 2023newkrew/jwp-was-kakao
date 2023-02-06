package webserver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpRequest {
    private HttpRequestHeader header;
    private String body;

    public String getRequestPath() {
        return header.getRequestPath();
    }

    public String getRequestMethod() {
        return header.getRequestMethod();
    }
}
