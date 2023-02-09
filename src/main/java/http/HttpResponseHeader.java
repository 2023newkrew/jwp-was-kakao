package http;

import enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HttpResponseHeader {
    private List<String> headers;

    public static HttpResponseHeader of(HttpStatus status, ContentType contentType, int contentLength) {
        List<String> headers = new ArrayList<>();

        headers.add(String.format("HTTP/1.1 %d %s ", status.value(), status.name()));
        headers.add(String.format("Content-Type: %s;charset=utf-8 ", contentType.getValue()));
        if (contentLength > 0) {
            headers.add(String.format("Content-Length: %s ", contentLength));
        }
        return new HttpResponseHeader(headers);
    }

    public static HttpResponseHeader create302FoundHeader(String redirectURI) {
        List<String> headers = new ArrayList<>();
        HttpStatus status = HttpStatus.FOUND;
        headers.add(String.format("HTTP/1.1 %d %s ", status.value(), status.name()));
        headers.add(String.format("Location: %s ", redirectURI));
        return new HttpResponseHeader(headers);
    }

    public void addHeader(String header) {
        headers.add(header);
    }
}
