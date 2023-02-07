package webserver.io;

import org.springframework.http.HttpMethod;
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
        String line = getDecodedLine();
        String[] headLines = line.split(DELIMITER);
        do {
            line = readLine();
        }
        while (isNotNullOrEmpty(line));

        HttpMethod httpMethod = HttpMethod.resolve(headLines[0]);
        URL URL = new URL(headLines[1]);

        return new Request(httpMethod, URL);
    }

    private String getDecodedLine() throws IOException {
        return URLDecoder.decode(readLine(), StandardCharsets.UTF_8);
    }

    private String readLine() throws IOException {
        String line = br.readLine();
        System.out.println(line);

        return line;
    }

    private boolean isNotNullOrEmpty(String line) {
        return Objects.nonNull(line) && !line.equals("");
    }

    @Override
    public void close() throws IOException {
        br.close();
    }
}
