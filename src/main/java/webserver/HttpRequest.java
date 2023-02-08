package webserver;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * BufferedReader를 주입 받아 RequestHeader 객체와 Map 자료형의 requestParams, String 자료형의 requestBody를 만듦.
 */
public class HttpRequest {
    private final BufferedReader bufferedReader;
    private RequestHeader requestHeader;
    private Map<String, String> requestParams = new HashMap<>();
    private String requestBody;

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        this.bufferedReader = bufferedReader;
        parseHeader();
        parseParams();
        parseBody();
    }

    public void parseParams() {
        String uri = requestHeader.get("URI").orElseThrow(IllegalArgumentException::new);
        // 요청에 파라미터가 없는 경우 requestParams는 null
        if (!uri.contains("?")) {
            requestParams = null;
            return;
        }

        requestParams = IOUtils.extractParams(uri.split("\\?")[1]);
    }

    private void parseHeader() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (!"".equals(line)) { // 공백(헤더와 바디의 구분선)이 나타날 때까지 line을 읽는다
            if (line == null) {
                throw new IOException();
            }
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }

        this.requestHeader = new RequestHeader(stringBuilder.toString());
    }

    private void parseBody() throws IOException {
        Integer contentLength = Integer.valueOf(requestHeader.get("Content-Length").orElse("0"));
        this.requestBody = IOUtils.readData(bufferedReader, contentLength);
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public Set<String> getParamKeySet() {
        return requestParams.keySet();
    }

    public String getParam(String key) {
        return requestParams.get(key);
    }
}
