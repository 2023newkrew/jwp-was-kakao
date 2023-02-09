package webserver.request;

import lombok.Getter;
import utils.IOUtils;
import webserver.Cookie;
import webserver.constant.HttpHeaderProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HttpRequest {

    private HttpRequestTarget target;

    private HttpRequestHeaders headers;

    private Session session;

    private List<Cookie> cookies = new ArrayList<>();

    private String body;

    public HttpRequest(BufferedReader reader) throws IOException {
        this.target = new HttpRequestTarget(reader);
        this.headers = new HttpRequestHeaders(reader);
        if (headers.containsKey("Cookie")) {
            parseCookie(headers.get("Cookie"));
        }
        parseBody(reader);
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private void parseCookie(String cookies) {
        this.cookies = Arrays.stream(cookies.split(";"))
                .map(String::trim)
                .map(Cookie::new)
                .collect(Collectors.toList());
    }

    private void parseBody(BufferedReader reader) throws IOException {
        if (headers.containsKey(HttpHeaderProperties.CONTENT_LENGTH.getKey())) {
            int contentLength = Integer.parseInt(headers.get(HttpHeaderProperties.CONTENT_LENGTH.getKey()));
            this.body = IOUtils.readData(reader, contentLength);
        }
    }
}
