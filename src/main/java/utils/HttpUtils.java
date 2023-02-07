package utils;

import http.request.CustomHttpMethod;
import http.request.CustomHttpRequest;
import http.response.CustomHttpResponse;
import http.response.CustomHttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static CustomHttpRequest readHttpRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null || "".equals(line)) {
            throw new RuntimeException("잘못된 요청 형식입니다.");
        }
        String[] tokens = line.split(" ");

        CustomHttpMethod customHttpMethod = CustomHttpMethod.from(tokens[0]);

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
        if (customHttpMethod == CustomHttpMethod.POST) {
            body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
        }

        return new CustomHttpRequest(customHttpMethod, url, query, protocol, headers, body);
    }

    public static void respondHttpResponse(DataOutputStream dos, CustomHttpResponse response){
        try {
            CustomHttpStatus status = response.getHttpStatus();
            String statusLine = String.join(" ",
                    response.getProtocol(),
                    String.valueOf(status.getValue()),
                    status.getReasonPhrase());
            dos.writeBytes(statusLine + " \r\n");

            for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                dos.writeBytes(k + ": " + v + " \r\n");
            }

            dos.writeBytes("\r\n");
            dos.write(response.getBody().getBytes(), 0, response.getBody().getBytes().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
