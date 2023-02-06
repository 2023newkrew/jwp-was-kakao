package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestParser {
    private static final String HEADER_SEPARATOR = ": ";

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line = br.readLine();
        String[] lineParts = line.split(" ");

        HttpMethod httpMethod = HttpMethod.valueOf(lineParts[0]);
        String uri = lineParts[1];
        String httpVersion = lineParts[2];

        Map<String, String> headers = new HashMap<>();
        List<String> body = new ArrayList<>();

        while ((line = br.readLine()) != null && line.length() > 0) {
            int headerSeparatorIndex = line.indexOf(HEADER_SEPARATOR);
            String headerName = line.substring(0, headerSeparatorIndex);
            String headerValue = line.substring(headerSeparatorIndex + HEADER_SEPARATOR.length());
            headers.put(headerName, headerValue);
        }

        while ((line = br.readLine()) != null) {
            body.add(line);
        }

        return new HttpRequest(httpMethod, uri, httpVersion, headers, body);
    }
}
