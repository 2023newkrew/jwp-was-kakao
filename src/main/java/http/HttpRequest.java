package http;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

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

    public List<HttpCookie> getCookies() {
        return header.getCookies();
    }

    public Optional<HttpCookie> getSessionCookie(){
        return header.getSessionCookie();
    }
}
