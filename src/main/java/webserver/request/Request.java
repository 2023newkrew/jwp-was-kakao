package webserver.request;

import org.springframework.http.HttpMethod;
import utils.IOUtils;

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
        System.out.println(header);

        this.body = new RequestBody(IOUtils.readData(br, header.getContentLength()), header.getContentType());
        if (!body.isEmpty()) System.out.println(body);
    }

    public RequestHeader getHeader() {
        return header;
    }

    public RequestBody getBody() {
        return body;
    }

    public String getPath() {
        return header.getPath();
    }

    public boolean hasStaticPath() {
        return header.hasStaticPath();
    }

    public String getAccept() {
        return header.getAccept();
    }

    public String getContentType() {
        return header.getContentType();
    }

    public String getVersion() {
        return header.getVersion();
    }

    public String getQueryParam(String key) {
        return header.getQueryParam(key);
    }

    public HttpMethod getHttpMethod() {
        return header.getHttpMethod();
    }

    public String getFormData(String key) {
        return body.getFormData(key);
    }
}
