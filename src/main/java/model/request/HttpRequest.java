package model.request;

import constant.DefaultConstant;
import model.annotation.Api;
import model.enumeration.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static constant.DefaultConstant.*;

@Getter
@AllArgsConstructor
@Builder
public class HttpRequest {
    private HttpMethod method;
    private String protocol;
    private String URL;
    private QueryParams queryParams;
    private RequestHeader header;
    private RequestBody body;

    public boolean methodAndURLEquals(Api annotation) {
        return method.equals(annotation.method()) && URL.equals(annotation.url());
    }

    public boolean isNotDefaultURL() {
        return !URL.equals(DEFAULT_URL);
    }
}
