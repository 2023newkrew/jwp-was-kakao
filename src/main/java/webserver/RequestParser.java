package webserver;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * BufferedReader를 주입 받아 RequestHeader 객체와 String 자료형의 requestBody를 만듦.
 */
public class RequestParser {
    private final BufferedReader bufferedReader;
    private RequestHeader requestHeader;
    private String requestBody;

    public RequestParser(BufferedReader bufferedReader) throws IOException {
        this.bufferedReader = bufferedReader;
        parseHeader();
        parseBody();
    }

    private void parseHeader() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (!"".equals(line)) {
            if (line == null) {
                throw new IOException();
            }
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }

        RequestHeader requestHeader1 = new RequestHeader(stringBuilder.toString());
        this.requestHeader = requestHeader1;
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

    public Map<String, String> getParams() {
        String uri = requestHeader.get("URI").orElseThrow(IllegalArgumentException::new);
        Map<String, String> params = new HashMap<>();

        if (requestHeader.get("method").get().equals("GET")) {
            if (!uri.contains("?")) {
                return null;
            }
            String split = uri.split("\\?")[1];
            extractParams(split, params);
        } else if (requestHeader.get("method").get().equals("POST")) {
            extractParams(requestBody, params);
        }
        return params;
    }

    private void extractParams(String requestBody, Map<String, String> params) {
        String[] datas = requestBody.split("&");
        for (String data : datas) {
            String[] aaa = data.split("=");
            params.put(aaa[0], aaa[1]);
        }
    }
}
