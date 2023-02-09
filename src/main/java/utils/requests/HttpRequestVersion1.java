package utils.requests;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * HttpRequestVersion1 is for parsing request written in HTTP/1.1
 */
public class HttpRequestVersion1 implements HttpRequest {
    private HttpRequestFirstLine httpRequestFirstLine;
    private final Map<String, String> httpHeader = new HashMap<>();
    private String body;

    /**
     * cannot make instance through constructor. use HttpRequestVersion1.readFrom method instead.
     */
    private HttpRequestVersion1(){}

    /**
     * read primitive Http request and parse it into HttpRequest instance.
     * @param br where to read.
     * @return parsed HttpRequest
     * @throws IOException when reading data from bufferedreader has problem.
     */
    public static HttpRequest readFrom(BufferedReader br) throws IOException {
        HttpRequestVersion1 httpRequestVersion1 = new HttpRequestVersion1();
        readFirstLine(httpRequestVersion1, br);
        readHeader(httpRequestVersion1, br);
        readBody(httpRequestVersion1, br);
        return httpRequestVersion1;
    }
    private static void readFirstLine(HttpRequestVersion1 httpRequestVersion1, BufferedReader br) throws IOException {
        String s = decodeURLString(br.readLine(), StandardCharsets.UTF_8);
        String[] tokens = s.split(" "); // GET URL HTTP/1.1
        httpRequestVersion1.httpRequestFirstLine = new HttpRequestFirstLine(
                RequestMethod.valueOf(tokens[0].toUpperCase()),
                URI.create(tokens[1]),
                tokens[2]
        );
    }
    private static void readHeader(HttpRequestVersion1 httpRequestVersion1, BufferedReader br) throws IOException {
        String headerLine;
        do {
            headerLine = decodeURLString(br.readLine(), StandardCharsets.UTF_8);
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
            httpRequestVersion1.body = decodeURLString(IOUtils.readData(br, contentLength), StandardCharsets.UTF_8);
        } catch(NumberFormatException e){
            // No attribute of Content-Length, so there isn't body in request.
            httpRequestVersion1.body = "";
        }
    }

    private static String decodeURLString(String originalStr, Charset encoding){
        return URLDecoder.decode(originalStr, encoding);
    }

    @Override
    public URI getURI() {
        return httpRequestFirstLine.getUri();
    }

    @Override
    public String getHttpVersion() {
        return httpRequestFirstLine.getHttpVersion();
    }

    @Override
    public RequestMethod getRequestMethod() {
        return httpRequestFirstLine.getRequestMethod();
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
