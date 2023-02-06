package model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HttpRequest {
    private String method;
    private String protocol;
    private String URL;
    private QueryParams queryParams;
    private RequestHeader header;
    private RequestBody body;
}
