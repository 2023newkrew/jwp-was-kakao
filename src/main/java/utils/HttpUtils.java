package utils;

import model.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public static final String QUERY_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";
    private static final String HEADER_DELIMITER = ": ";
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";

    public static CustomHttpRequest createHttpRequest(BufferedReader br) throws IOException {
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
            body = getBody(IOUtils.readData(br, Integer.parseInt(contentLength)));
        }

        return new CustomHttpRequest(httpRequestLine, headers, body);
    }

    public static void respond(DataOutputStream dos, CustomHttpResponse response){
        try {
            dos.writeBytes(response.getHttpStatus().getLine() + " \r\n");
            response.getHeaders().respond(dos);
            dos.writeBytes("\r\n");
            dos.write(response.getBody().getBytes(), 0, response.getBody().getBytes().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    
    private static CustomHttpRequestBody getBody(String requestBody) {
        return new CustomHttpRequestBody(Arrays.stream(requestBody.split(QUERY_DELIMITER))
                .map(field -> field.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(f -> f[0], f -> f[1])));
    }

}
