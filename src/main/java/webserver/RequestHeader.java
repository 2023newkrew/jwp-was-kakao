package webserver;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class RequestHeader {

    private HttpMethod httpMethod;

    private String url;

    private Map<String, String> queryParams;

    private String httpVersion;

    private Map<String, String> headers;


    public void convertToAbsolutePath(ResourceType resourceType) {
        url = resourceType.getPath() + url;
    }

    public boolean hasContentLength() {
        return headers.containsKey("Content-Length");
    }

    public String getExtension() {
        String[] splitedUrl = url.split("\\.");
        return splitedUrl[splitedUrl.length - 1];
    }

    public Optional<String> getContentType() {
        if (headers.containsKey("Accept")) {
            return Optional.ofNullable(headers.get("Accept").split(",")[0]);
        }

        return Optional.empty();
    }

    public boolean checkRequest(HttpMethod httpMethod, String url) {
        return this.httpMethod.equals(httpMethod)
                && this.url.equals(url);
    }
}
