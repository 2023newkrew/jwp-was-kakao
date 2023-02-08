package webserver.request;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestBody {
    private final String requestBody;

    private RequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public static RequestBody parse(BufferedReader reader, RequestHeader requestHeader) throws IOException {
        if (requestHeader.checkRequestBodyExists()) {
            int contentLength = requestHeader.findRequestBodySize();
            return new RequestBody(IOUtils.readData(reader, contentLength));
        }
        return new RequestBody("");
    }

    public Map<String, String> parseRequestBody() {
        return Arrays.stream(requestBody.split("&"))
            .map(s -> s.split("="))
            .collect(Collectors.toMap(keyValuePair -> keyValuePair[0], keyValuePair -> keyValuePair[1], (a, b) -> b));
    }
}
