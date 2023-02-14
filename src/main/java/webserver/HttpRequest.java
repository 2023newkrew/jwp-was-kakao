package webserver;

import exception.ErrorCode;
import exception.WasException;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * BufferedReader를 주입 받아 RequestHeader 객체와 Map 자료형의 requestParams, String 자료형의 requestBody를 만듦.
 */
public class HttpRequest {
    private final BufferedReader bufferedReader;
    private RequestHeader requestHeader;
    private Map<String, String> requestParams = new HashMap<>();
    private String requestBody;

    public HttpRequest(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        parseHeader();
        parseParams();
        parseBody();
    }

    public void parseParams() {
        String uri = requestHeader.get("URI")
                .orElseThrow(() -> new WasException(ErrorCode.BAD_REQUEST));
        // 요청에 파라미터가 없는 경우 requestParams는 null
        if (!uri.contains("?")) {
            requestParams = null;
            return;
        }

        requestParams = IOUtils.extractParams(uri.split("\\?")[1]);
    }

    private void parseHeader() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line = bufferedReader.readLine();
            while (!"".equals(line)) { // 공백(헤더와 바디의 구분선)이 나타날 때까지 line을 읽는다
                if (line == null) {
                    throw new WasException(ErrorCode.BAD_REQUEST);
                }
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new WasException(ErrorCode.BAD_REQUEST);
        }

        this.requestHeader = new RequestHeader(stringBuilder.toString());
    }

    private void parseBody() {
        int contentLength = Integer.parseInt(requestHeader.get("Content-Length").orElse("0"));
        try {
            Optional<String> data = Optional.ofNullable(IOUtils.readData(bufferedReader, contentLength));
            this.requestBody = data.orElse("");
        } catch (IOException e) {
            throw new WasException(ErrorCode.CAN_NOT_READ_DATA);
        }
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getParam(String key) {
        return requestParams.get(key);
    }
}
