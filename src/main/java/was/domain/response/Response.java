package was.domain.response;

import lombok.Builder;


@Builder
public class Response {
    private final Version version;
    private final StatusCode statusCode;
    private final String contentType;
    private final String location;
    private final byte[] body;
    
    public byte[] getBody() {
        if (body == null || body.length == 0) {
            return new byte[]{};
        }
        return body;
    }
    
    public String getHeader(){
        return String.join("", getResponseLine(), getContentType(), getContentLength(), getLocation(), "\r\n");
    }

    private String getResponseLine() {
        return String.format("%s %s \r%n", version.getName(), statusCode.toString());
    }

    private String getContentType() {
        if(contentType == null || contentType.isEmpty())
            return "";

        return String.format("Content-Type: %s \r%n", contentType);
    }

    private String getContentLength() {
        if (body == null || body.length == 0) {
            return "";
        }
        return String.format("Content-Length: %d \r%n", body.length);
    }

    private String getLocation() {
        if(location == null || location.length() == 0){
            return "";
        }

        return String.format("Location: %s", location);
    }
}