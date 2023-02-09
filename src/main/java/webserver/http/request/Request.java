package webserver.http.request;

import org.springframework.http.HttpMethod;
import utils.IOUtils;
import webserver.http.Cookie;
import webserver.http.request.support.StaticDirectory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private RequestHeader header;
    private RequestBody body;

    public Request(BufferedReader br) throws IOException {
        String line = br.readLine();
        List<String> headerLines = new ArrayList<>();
        while(line != null && !"".equals(line)) {
            headerLines.add(line);
            line = br.readLine();
        }

        this.header = new RequestHeader(headerLines);
        this.body = new RequestBody(IOUtils.readData(br, header.getContentLength()));
        System.out.println(header + "\n" + body + "\n" + header.getCookie());
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

    public Cookie getCookie() {
        return header.getCookie();
    }

    public boolean hasStaticPath() {
        if (getPath() == null) return false;
        String[] pathTokens = getPath().split("/");
        if (pathTokens.length < 2) return false;
        return StaticDirectory.resolve(pathTokens[1].toUpperCase()) != null;
    }
}
