package utils;

import model.HttpMethod;
import model.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {
    public static HttpRequest createHttpRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null || "".equals(line)) {
            throw new RuntimeException("잘못된 요청 형식입니다.");
        }
        String[] tokens = line.split(" ");

        HttpMethod httpMethod = HttpMethod.from(tokens[0]);

        String url = tokens[1];
        String[] urls = url.split("\\?");
        url = urls[0];

        Map<String, String> query = new HashMap<>();
        if (urls.length > 1) {
            String queryString = urls[1];
            Arrays.stream(queryString.split("&"))
                    .forEach(field -> {
                        String[] fields = field.split("=");
                        query.put(fields[0], fields[1]);
                    });
        }

        String protocol = tokens[2];

        line = br.readLine();
        Map<String, String> headers = new HashMap<>();
        while (line != null && !"".equals(line)) {
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
            line = br.readLine();
        }

        String body = "";
        if (httpMethod == HttpMethod.POST) {
            body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        }

        return new HttpRequest(httpMethod, url, query, protocol, headers, body);
    }

}
