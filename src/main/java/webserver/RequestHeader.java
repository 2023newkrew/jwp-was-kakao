package webserver;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

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
//        if (resourceType == ResourceType.STATIC) {
//            url = resourceType.getPath() + url;//.substring(1);
//            return;
//        }
        url = resourceType.getPath() + url;

    }

    public boolean hasContentLength() {
        return headers.containsKey("Content-Length");
    }
}
