package was.domain.response;

import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class Response {

    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String LOCATION = "Location";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String SET_COOKIE = "Set-Cookie";

    private final Version version;
    private final StatusCode statusCode;
    private final Map<String, String> headerMap = new HashMap<>();
    private final byte[] body;

    @Builder
    public Response(Version version, StatusCode statusCode, String contentType, String location, String cookie, byte[] body) {
        this.version = version;
        this.statusCode = statusCode;
        this.body = body;
        setHeader(CONTENT_TYPE, contentType);
        setHeader(SET_COOKIE, cookie);
        setHeader(LOCATION, location);
        if (isValidBody()) {
            setHeader(CONTENT_LENGTH, String.valueOf(body.length));
        }
    }

    public static ResponseBuilder htmlBuilder() {
        return Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.OK)
                .contentType("text/html;charset=utf-8");
    }

    public static Optional<Response> htmlFromFile(String filePath) {
        try {
            return html(FileIoUtils.loadFileFromClasspath(filePath));
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<Response> html(String body) {
        return html(body.getBytes());
    }

    public static Optional<Response> html(byte[] body) {
        return Optional.of(Response.htmlBuilder()
                .body(body)
                .build());
    }

    public static Optional<Response> cssFromFile(String filePath) {
        try {
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .contentType("text/css")
                    .body(FileIoUtils.loadFileFromClasspath(filePath))
                    .build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<Response> redirection(String location) {
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .location(location)
                .build());
    }

    public static Optional<Response> redirection(String location, String cookie) {
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .location(location)
                .cookie(cookie)
                .build());
    }

    private void setHeader(String header, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        headerMap.put(header, value);
    }

    public String getResponseLine() {
        return String.format("%s %s\r\n", version.getName(), statusCode.toString());
    }
    
    public String getHeader(){
        return headerMap.keySet().stream()
                .map(header -> header + ": " + headerMap.get(header))
                .collect(Collectors.joining("\r\n"));
    }

    public boolean isValidBody() {
        return body != null && body.length > 0;
    }

    public byte[] getBody() {
        if (body == null || body.length == 0) {
            return new byte[]{};
        }
        return body;
    }
}