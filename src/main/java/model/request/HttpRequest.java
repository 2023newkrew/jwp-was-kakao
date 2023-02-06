package model.request;

import constant.HttpMethod;
import constant.HttpProtocol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class HttpRequest {
    private String method;
    private RequestURL requestURL;
    private QueryString queryString;
    private String protocol;
    private RequestHeader header;
    private RequestBody body;
}
