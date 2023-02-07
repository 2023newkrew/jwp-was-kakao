package utils;

import http.Body;
import http.HttpHeaders;
import http.Protocol;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.request.HttpUrl;
import http.request.RequestInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

import static http.HttpHeaders.CONTENT_LENGTH;

public class HttpRequestUtils {

    private static final String ZERO = "0";

    public static HttpRequest createHttpRequest(BufferedReader br) throws IOException {
        RequestInfo requestInfo = createRequestInfo(br.readLine());
        HttpHeaders httpHeaders = createHeader(br);
        Body body = new Body(IOUtils.readData(br, Integer.parseInt(httpHeaders.getOrDefault(CONTENT_LENGTH, ZERO))));

        return new HttpRequest(requestInfo, httpHeaders, body);
    }

    private static HttpHeaders createHeader(BufferedReader br) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        while (true) {
            String line = br.readLine();
            if (Objects.isNull(line) || "".equals(line)) {
                break;
            }

            httpHeaders.put(line.trim());
        }

        return httpHeaders;
    }

    private static RequestInfo createRequestInfo(String rawRequestInfo) {
        String[] requestInfo = rawRequestInfo.split(" ");
        HttpMethod httpMethod = HttpMethod.valueOf(requestInfo[0]);
        HttpUrl url = new HttpUrl(requestInfo[1]);
        Protocol protocol = Protocol.from(requestInfo[2]);

        return new RequestInfo(httpMethod, url, protocol);
    }

}
