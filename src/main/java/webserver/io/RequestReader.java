package webserver.io;

import utils.IOUtils;
import webserver.request.Request;
import webserver.request.RequestBody;
import webserver.request.RequestHeader;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestReader implements Closeable {

    private final BufferedReader br;

    public RequestReader(InputStream in) {
        InputStreamReader isr = new InputStreamReader(in);
        this.br = new BufferedReader(isr);
    }

    public Request read() throws IOException {
        RequestHeader header = readHeader();
        RequestBody body = readBody(header.getContentLength());

        return new Request(header, body);
    }

    private RequestHeader readHeader() throws IOException {
        String head = decode(readLine());
        List<String> headers = readLines();

        return new RequestHeader(head, headers);
    }

    private String decode(String requestBody) {
        return URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
    }

    private String readLine() throws IOException {
        String line = br.readLine();
        System.out.println(line);

        return line;
    }

    private List<String> readLines() throws IOException {
        List<String> headers = new ArrayList<>();
        String line = readLine();
        while (isNotNullOrBlank(line)) {
            headers.add(line);
            line = readLine();
        }

        return headers;
    }

    private RequestBody readBody(int contentLength) throws IOException {
        if (contentLength == 0) {
            return null;
        }

        return new RequestBody(readData(contentLength));
    }

    private String readData(int contentLength) throws IOException {
        String data = IOUtils.readData(br, contentLength);
        System.out.println(data);

        return decode(data);

    }

    private boolean isNotNullOrBlank(String line) {
        return Objects.nonNull(line) && !line.isBlank();
    }

    @Override
    public void close() throws IOException {
        br.close();
    }
}
