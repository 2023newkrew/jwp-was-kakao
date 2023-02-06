package webserver;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * BufferedReader를 주입 받아 RequestHeader 객체와 String 자료형의 requestBody를 만듦.
 */
public class RequestParser {
    private final BufferedReader bufferedReader;
    private RequestHeader requestHeader;
    private Map<String, String> requestParams = new HashMap<>();
    private String requestBody;

    public RequestParser(BufferedReader bufferedReader) throws IOException {
        this.bufferedReader = bufferedReader;
        parseParams();
        parseHeader();
        parseBody();
    }

    public void parseParams() {
        String uri = requestHeader.get("URI").orElseThrow(IllegalArgumentException::new);
        // 요청에 파라미터가 없는 경우 requestParams는 null
        if (!uri.contains("?")) {
            requestParams = null;
        }

        String requestMethod = requestHeader.get("method").get();
        if (requestMethod.equals("GET")) {
            extractParams(uri.split("\\?")[1]);
        }
        if (requestMethod.equals("POST")) {
            extractParams(requestBody);
        }
    }

    // 쿼리스트링으로부터 key와 value를 추출하여 requestParams Map에 추가
    private void extractParams(String queryString) {
        String[] datas = queryString.split("&");
        for (String data : datas) {
            String[] strArr = data.split("=");
            requestParams.put(strArr[0], strArr[1]);
        }
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
