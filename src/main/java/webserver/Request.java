package webserver;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * BufferedReader를 주입 받아 RequestHeader 객체와 String 자료형의 requestBody를 만듦.
 */
public class Request {
    private final BufferedReader bufferedReader;
    private RequestHeader requestHeader;
    private String requestBody;
    private Map<String, String> requestParams;

    public Request(BufferedReader bufferedReader) throws IOException {
        this.bufferedReader = bufferedReader;
        parseHeader();
        parseBody();
        parseParams();
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

        this.requestHeader = new RequestHeader(stringBuilder.toString());
    }

    private void parseBody() throws IOException {
        int contentLength = Integer.parseInt(requestHeader.get("Content-Length").orElse("0"));
        this.requestBody = IOUtils.readData(bufferedReader, contentLength);
        if (requestHeader.getMethod().equals("GET")) {
            requestBody = requestHeader.parseUriParams();
        }
    }

    private void parseParams() {
        if (requestBody == null || !requestBody.contains("=")) {
            requestParams = null;
            return;
        }
        extractParams(requestBody);
    }

    private void extractParams(String requestParams) {
        Map<String, String> params = new HashMap<>();
        String[] sets = requestParams.split("&");
        for (String set : sets) {
            String[] split = set.split("=");
            params.put(split[0], split[1]);
        }
        this.requestParams = params;
    }

    public Map<String, String> getRequestParams() {
        return this.requestParams;
    }

    public String getUri() { return requestHeader.getUri(); }

}
