package utils.requests;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HttpRequestVersion1 implements HttpRequest {
    private RequestMethod requestMethod;
    private URI uri;
    private String httpVersion;
    private final Map<String, String> httpHeader = new HashMap<>();
    private String body;

    private HttpRequestVersion1(){}

    public static HttpRequestVersion1 readFrom(BufferedReader br) throws IOException {
        HttpRequestVersion1 httpRequestVersion1 = new HttpRequestVersion1();
        readFirstLine(httpRequestVersion1, br);
        readHeader(httpRequestVersion1, br);
        readBody(httpRequestVersion1, br);
        return httpRequestVersion1;
    }
    private static void readFirstLine(HttpRequestVersion1 httpRequestVersion1, BufferedReader br) throws IOException {
        String s = br.readLine();
        String[] tokens = s.split(" "); // GET URL HTTP/1.1
        httpRequestVersion1.requestMethod = RequestMethod.valueOf(tokens[0].toUpperCase());
        httpRequestVersion1.uri = URI.create(tokens[1]);
        httpRequestVersion1.httpVersion = tokens[2];
    }
    private static void readHeader(HttpRequestVersion1 httpRequestVersion1, BufferedReader br) throws IOException {
        String headerLine;
        do {
            headerLine = br.readLine();
            updateHeaderProperty(httpRequestVersion1, headerLine);
        } while(!("".equals(headerLine) || Objects.isNull(headerLine)));
    }

    private static void updateHeaderProperty(HttpRequestVersion1 httpRequestVersion1, String headerLine){
        String[] headerLineSplit = Optional.ofNullable(headerLine).orElse("").split(":");
        if (headerLineSplit.length > 1) {
            httpRequestVersion1.httpHeader.put(headerLineSplit[0].trim(), headerLineSplit[1].trim());
        }
    }
    private static void readBody(HttpRequestVersion1 httpRequestVersion1, BufferedReader br) throws IOException {
        try {
            int contentLength = Integer.parseInt(httpRequestVersion1.getHeaderParameter("Content-Length"));
            httpRequestVersion1.body = IOUtils.readData(br, contentLength);
        } catch(NumberFormatException e){
            // No attribute of Content-Length, so there isn't body in request.
            httpRequestVersion1.body = "";
        }
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public String getHeaderParameter(String parameter) {
        return httpHeader.get(parameter);
    }
}
