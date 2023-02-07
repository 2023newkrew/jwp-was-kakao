package dto;

import java.util.HashMap;
import java.util.Map;
import webserver.request.StatusCode;
import webserver.response.Response;

public class BaseResponseDto {

    private final StatusCode statusCode;
    private final String body;

    public BaseResponseDto(StatusCode statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public Response convertToResponse(String version, String contentType) {
        Map<String, String> headers = new HashMap<>() {{
            put("Content-Type", contentType);
            put("Content-Length", String.valueOf(body.getBytes().length));
        }};

        if (this.statusCode == StatusCode.FOUND) {
            headers.put("Location", "/index.html");
        }
        return new Response(statusCode, body, headers, version);
    }
}

