package was.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import was.annotation.RequestMethod;
import was.domain.Cookie;
import was.domain.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtils {

    public static Request getRequest(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String requestLine = reader.readLine();
            String method = requestLine.split(" ")[0];
            PathParamsParser pathParamsParser = new PathParamsParser(requestLine.split(" ")[1]);
            Map<String, String> header = getHeader(reader);
            String requestBody = getBody(reader, Integer.parseInt(header.getOrDefault("Content-Length", "0")));
            Cookie cookie = getCookie(header.getOrDefault("Cookie", ""));

            return Request.builder()
                    .method(RequestMethod.valueOf(method))
                    .path(pathParamsParser.getPath())
                    .params(pathParamsParser.getParams())
                    .body(requestBody)
                    .cookie(cookie)
                    .build();
        } catch (IOException ignored) {

        }
        return null;
    }

    private static Map<String, String> getHeader(BufferedReader reader) throws IOException {
        return getLines(reader).stream()
                .map(it -> it.split(": ", 2))
                .collect(Collectors.toMap(it -> it[0], it -> it[1]));
    }

    private static List<String> getLines(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line = reader.readLine();
        while (line != null && !"".equals(line)) {
            lines.add(line);
            line = reader.readLine();
        }
        return lines;
    }

    private static String getBody(BufferedReader reader, Integer contentLength) throws IOException {
        return String.join("\r\n", IOUtils.readData(reader, contentLength));
    }

    private static Cookie getCookie(String cookieHeader) {
        if (cookieHeader.isEmpty()) return null;

        String[] values = cookieHeader.split("; ");

        String uuid = values[0].split("=", 2)[1];
        String path = values[1].split("=", 2)[1];

        return new Cookie(uuid, path);
    }
}
