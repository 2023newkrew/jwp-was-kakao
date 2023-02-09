package framework.request;

import java.util.Map;

/**
 * BufferedReader를 주입 받아 RequestHeader 객체와 String 자료형의 requestBody를 만듦.
 */
public class Request {
    private final RequestHeader requestHeader;
    private final Map<String, String> requestParams;

    public Request(RequestHeader requestHeader, Map<String, String> requestParams) {
        this.requestHeader = requestHeader;
        this.requestParams = requestParams;
    }

    public Map<String, String> getRequestParams() {
        return this.requestParams;
    }

    public String getUri() {
        return requestHeader.getUri();
    }

    public String getMethod() {
        return requestHeader.getMethod();
    }

}
