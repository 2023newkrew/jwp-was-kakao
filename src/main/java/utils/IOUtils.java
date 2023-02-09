package utils;

import exceptions.InvalidQueryParameterException;
import http.HttpResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IOUtils {
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static Map<String, String> extractParamMapFromPath(String path) {
        String[] token = path.split("\\?");
        try {
            return IOUtils.extractParamsMap(token[1]);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidQueryParameterException();
        }
    }

    public static Map<String, String> extractParamsMap(String params) throws IndexOutOfBoundsException {
        String[] queryParams = params.split("&");
        return Arrays.stream(queryParams)
                .map(s -> s.split("="))
                .collect(Collectors.toMap(
                        a -> a[0],
                        a -> URLDecoder.decode(a[1], StandardCharsets.UTF_8)
                ));
    }

    public static void writeToOutputStream(DataOutputStream dos, HttpResponse response) throws IOException {
        writeHeaderToOutputStream(dos, response);
        writeBodyToOutputStream(dos, response);
    }

    private static void writeHeaderToOutputStream(DataOutputStream dos, HttpResponse response) throws IOException {
        response.getHeaders()
                .getHeaders()
                .stream()
                .forEach(header -> {
                    try {
                        dos.writeBytes(header + "\r\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        dos.writeBytes("\r\n");
    }

    private static void writeBodyToOutputStream(DataOutputStream dos, HttpResponse response) throws IOException {
        byte[] body = response.getBody();
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
