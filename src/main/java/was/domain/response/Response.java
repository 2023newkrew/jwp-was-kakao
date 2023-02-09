package was.domain.response;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class Response {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String LOCATION = "Location";
    public static final String CONTENT_LENGTH = "Content-Length";

    private final Version version;
    private final StatusCode statusCode;
    private final Map<String, String> headerMap = new HashMap<>();
    private final byte[] body;

    @Builder
    public Response(Version version, StatusCode statusCode, String contentType, String location, byte[] body) {
        this.version = version;
        this.statusCode = statusCode;
        this.body = body;
        setHeader(CONTENT_TYPE, contentType);
        setHeader(LOCATION, location);
        if (isValidBody()) {
            setHeader(CONTENT_LENGTH, String.valueOf(body.length));
        }
    }

    public static ResponseBuilder cssBuilder() {
        return Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.OK)
                .contentType("text/css");
    }

    public static ResponseBuilder htmlBuilder() {
        return Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.OK)
                .contentType("text/html;charset=utf-8");
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