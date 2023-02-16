package model.http;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomHttpRequestFactory {

    public static final String QUERY_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";
    private static final String HEADER_DELIMITER = ": ";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";

    public static CustomHttpRequest generateHttpRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        CustomHttpRequestLine httpRequestLine = new CustomHttpRequestLine(line);

        line = br.readLine();
        CustomHttpHeader headers = new CustomHttpHeader();
        while (line != null && !line.isEmpty()) {
            String[] header = line.split(HEADER_DELIMITER);
            headers.put(header[0], header[1]);
            line = br.readLine();
        }

        String contentLength = headers.get(CONTENT_LENGTH_HEADER);
        CustomHttpRequestBody body = null;
        if (contentLength != null) {
            String requestBody = IOUtils.readData(br, Integer.parseInt(contentLength));
            body = new CustomHttpRequestBody(Arrays.stream(requestBody.split(QUERY_DELIMITER))
                    .map(field -> field.split(KEY_VALUE_DELIMITER))
                    .collect(Collectors.toMap(f -> f[0], f -> f[1])));
        }

        return new CustomHttpRequest(httpRequestLine, headers, body);
    }

}
