package webserver.io;

import org.springframework.http.HttpMethod;
import webserver.http.Headers;
import webserver.http.request.Request;
import webserver.http.request.path.URL;
import webserver.utils.IOUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class RequestReader implements Closeable {

    public static final String DELIMITER = " ";

    private final BufferedReader br;

    public RequestReader(InputStream in) {
        InputStreamReader isr = new InputStreamReader(in);
        this.br = new BufferedReader(isr);
    }

    public Request read() throws IOException {
        String line = readDecodedLine();
        String[] headLines = line.split(DELIMITER);
        Headers headers = getHeaders();
        String contentLength = headers.get("Content-Length");
        String requestBody = readRequestBody(contentLength);

        HttpMethod httpMethod = HttpMethod.resolve(headLines[0]);
        URL URL = new URL(headLines[1]);

        return new Request(httpMethod, URL, requestBody);
    }

    private String readRequestBody(String contentLength) throws IOException {
        if (Objects.isNull(contentLength)) {
            return null;
        }
        String requestBody = IOUtils.readData(br, Integer.parseInt(contentLength));
        requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
        System.out.println(requestBody);
        return requestBody;
    }

    private Headers getHeaders() throws IOException {
        Headers headers = new Headers();
        String line = readDecodedLine();
        while (isNotNullOrBlank(line)) {
            headers.put(line);
            line = readLine();
        }

        return headers;
    }

    private String readDecodedLine() throws IOException {
        return URLDecoder.decode(readLine(), StandardCharsets.UTF_8);
    }

    private String readLine() throws IOException {
        String line = br.readLine();
        System.out.println(line);

        return line;
    }

    private boolean isNotNullOrBlank(String line) {
        return Objects.nonNull(line) && !line.isBlank();
    }

    @Override
    public void close() throws IOException {
        br.close();
    }
}
