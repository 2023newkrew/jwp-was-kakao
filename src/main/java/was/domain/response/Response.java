package was.domain.response;

import lombok.Builder;


@Builder
public class Response {
    private final Version version;
    private final StatusCode statusCode;
    private final ResponseHeader responseHeader;
    private final byte[] body;

    public byte[] getBody() {
        if (body == null || body.length == 0) {
            return new byte[]{};
        }
        return body;
    }

    public String getHeader() {
        String result = String.join("", getResponseLine(), responseHeader.getHeader());

        return result;
    }

    private String getResponseLine() {
        return String.format("%s %s \r%n", version.getName(), statusCode.toString());
    }
}