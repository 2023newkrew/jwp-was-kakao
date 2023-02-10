package utils;

import http.HttpRequest;
import http.HttpRequestHeader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {
    public static HttpRequest parseRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        HttpRequestHeader header = extractRequestHeader(bufferedReader);
        String body = IOUtils.readData(bufferedReader, header.getContentLength()
                .orElse(0));
        return new HttpRequest(header, body);
    }

    public static HttpRequestHeader extractRequestHeader(BufferedReader bufferedReader) {
        try {
            List<String> headers = new ArrayList<>();
            String line;
            while (!"".equals(line = bufferedReader.readLine())) {
                headers.add(line);
                if (line == null) break;
            }
            return new HttpRequestHeader(headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
