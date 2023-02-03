package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static Map<String, String> getParameters(String uri) {
        uri = uri.split("\\?")[1];
        List<String> queryParams = List.of(uri.split("&"));
        Map<String, String> parameters = new HashMap<>();
        queryParams.forEach((queryParam) ->
                parameters.put(queryParam.split("=")[0], queryParam.split("=")[1]));

        return parameters;
    }

    public static byte[] getTargetBody(final String uri) {
        try {
            if (uri.endsWith(".html")) {
                return FileIoUtils.loadFileFromClasspath("./templates" + uri.replaceFirst("^\\.+", ""));
            } else if (uri.endsWith(".css")) {
                return FileIoUtils.loadFileFromClasspath("./static" + uri.replaceFirst("^\\.+", ""));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "".getBytes();
    }

    public static List<String> parseRequestMessage(final BufferedReader reader) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while (!"".equals(line = reader.readLine())){
                if (Objects.isNull(line)) break;
                lines.add(line);
                System.out.println(line);
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }

        return lines;
    }

    public static String getURI(final List<String> requestMessage) {
        return requestMessage.get(0).split(" ")[1];
    }
}
