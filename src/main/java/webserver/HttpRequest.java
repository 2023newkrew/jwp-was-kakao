package webserver;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import utils.IOUtils;

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
        if (headers.containsKey(HttpHeaders.CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(headers.get(HttpHeaders.CONTENT_LENGTH));
            this.body = IOUtils.readData(reader, contentLength);
        }
    }
}
