package http;

import enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HttpResponseHeader {
    private List<String> headers;

    public static HttpResponseHeader of(HttpStatus status, ContentType contentType, int contentLength) {
        List<String> headers = new LinkedList<>();

        headers.add(String.format("HTTP/1.1 %d %s", status.value(), status.name()));
        headers.add(String.format("Content-Type: %s;charset=utf-8", contentType.getValue()));
        if (contentLength > 0) {
            headers.add(String.format("Content-Length: %s", contentLength));
        }
        return new HttpResponseHeader(headers);
    }

    public static HttpResponseHeader create302FoundHeader(String redirectURI) {
        HttpStatus status = HttpStatus.FOUND;

        return new HttpResponseHeader(List.of(
                String.format("HTTP/1.1 %d %s", status.value(), status.name()),
                String.format("Location: %s", redirectURI)
        ));
    }
}
