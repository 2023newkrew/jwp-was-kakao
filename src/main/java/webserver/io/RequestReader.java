package webserver.io;

import org.springframework.http.HttpMethod;
import webserver.request.Headers;
import webserver.request.Request;
import webserver.request.path.URL;

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

        String requestBody = null;
        if (headers.getContentLength() > 0) {
            requestBody = readDecodedLine();
        }

        HttpMethod httpMethod = HttpMethod.resolve(headLines[0]);
        URL URL = new URL(headLines[1]);

        return new Request(httpMethod, URL, requestBody);
    }

    private Headers getHeaders() throws IOException {
        Headers headers = new Headers();
        String line = readDecodedLine();
        while (isNotNullOrBlank(line)) {
            headers.add(line);
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
