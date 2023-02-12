package webserver.http.request;

import org.springframework.http.HttpMethod;
import utils.IOUtils;
import webserver.http.Cookie;
import webserver.http.Session;
import webserver.http.request.support.StaticDirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private RequestHeader header;
    private RequestBody body;
    private final Map<String, Object> attributes = new HashMap<>();

    public Request(BufferedReader br) throws IOException {
        String line = br.readLine();
        List<String> headerLines = new ArrayList<>();
        while(line != null && !"".equals(line)) {
            headerLines.add(line);
            line = br.readLine();
        }

        this.header = new RequestHeader(headerLines);
        this.body = new RequestBody(IOUtils.readData(br, header.getContentLength()));
    }

    public String getProtocol() {
        return header.getProtocol();
    }

    public HttpMethod getHttpMethod() {
        return header.getHttpMethod();
    }

    public String getURL() {
        return header.getURL();
    }

    public String getPath() {
        return header.getURL().split("\\?")[0];
    }

    public String getAccept() {
        return header.getAccept();
    }

    public String getBody() {
        return body.getBody();
    }

    public Cookie getCookies() {
        return header.getCookies();
    }

    public Session getSession() {
        return header.getSession();
    }

    public boolean hasSessionId() {
        return header.hasSessionId();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public boolean hasStaticPath() {
        if (getPath() == null) return false;
        String[] pathTokens = getPath().split("/");
        if (pathTokens.length < 2) return false;
        return StaticDirectory.resolve(pathTokens[1].toUpperCase()) != null;
    }

    public boolean isLogined() {
        return header.getSession().getAttribute("user") != null;
    }
}
