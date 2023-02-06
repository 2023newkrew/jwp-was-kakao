package utils;

import model.CustomHttpMethod;
import model.CustomHttpRequest;
import model.CustomHttpResponse;
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

    public static CustomHttpRequest createHttpRequest(BufferedReader br) throws IOException {
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

    public static void respond(DataOutputStream dos, CustomHttpResponse response){
        try {
            dos.writeBytes(response.getHttpStatus() + " \r\n");
            response.getHeaders().forEach((k, v) -> {
                try {
                    dos.writeBytes(k + ": " + v + " \r\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            dos.writeBytes("\r\n");
            dos.write(response.getBody().getBytes(), 0, response.getBody().getBytes().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
