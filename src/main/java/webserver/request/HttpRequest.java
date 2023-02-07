package webserver.request;

import lombok.Getter;
import utils.IOUtils;
import webserver.constant.HttpHeaderProperties;

import java.io.BufferedReader;
import java.io.IOException;

@Getter
public class HttpRequest {

    private HttpRequestTarget target;

    private HttpRequestHeaders headers;

    private String body;

    public HttpRequest(BufferedReader reader) throws IOException {
        this.target = new HttpRequestTarget(reader);
        this.headers = new HttpRequestHeaders(reader);
        parseBody(reader);
    }

    private void parseBody(BufferedReader reader) throws IOException {
        if (headers.containsKey(HttpHeaderProperties.CONTENT_LENGTH.getKey())) {
            int contentLength = Integer.parseInt(headers.get(HttpHeaderProperties.CONTENT_LENGTH.getKey()));
            this.body = IOUtils.readData(reader, contentLength);
        }
    }
}
