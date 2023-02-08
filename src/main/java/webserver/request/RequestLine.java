package webserver.request;

import webserver.common.FileType;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestLine {

    private static final String QUERY_STRING_DELIMITER = "\\?";
    private static final String FILETYPE_EXTENSION_DELIMITER = "\\.";

    private final Method method;
    private final String url;
    private final String protocol;

    private RequestLine(Method method, String url, String protocol) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
    }

    public static RequestLine parse(String input) {
        String[] parsedInput = input.split(" ");
        return new RequestLine(
                Method.of(parsedInput[0]),
                parsedInput[1],
                parsedInput[2]
        );
    }

    public String getPath() {
        return url.split(QUERY_STRING_DELIMITER)[0];
    }

    public boolean hasQueryString() {
        String[] splitUrl = url.split(QUERY_STRING_DELIMITER);
        return splitUrl.length != 1;
    }

    public String getQueryString() {
        return url.split(QUERY_STRING_DELIMITER)[1];
    }

    public Map<String, String> parseQueryString() {
        return Arrays.stream(getQueryString().split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(keyValuePair -> keyValuePair[0], keyValuePair -> keyValuePair[1], (a, b) -> b));
    }

    public FileType findRequestedFileType() {
        String path = getPath();
        String[] splitPath = path.split(FILETYPE_EXTENSION_DELIMITER);
        String fileExtension = splitPath[splitPath.length - 1];
        return FileType.findType(fileExtension);
    }

    public Method getMethod() {
        return method;
    }
}
