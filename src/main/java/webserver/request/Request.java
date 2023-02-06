package webserver.request;

import org.springframework.http.HttpMethod;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
