package model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static utils.Constants.CONTENT_LENGTH;
import static utils.Constants.LOCATION;

@Getter
public class HttpResponse {
    private static final String OK_STATUS_LINE = "HTTP/1.1 200 OK";
    private static final String REDIRECT_STATUS_ILNE = "HTTP/1.1 302 FOUND";

    private final String statusLine;
    private final Map<String, String> header;
    private final byte[] body;

    private HttpResponse(String statusLine, Map<String, String> header, byte[] body) {
        this.statusLine = statusLine;
        this.body = body;
        Map<String, String> newHeader = new HashMap<>(header);
        if(body.length > 0) {
            newHeader.put(CONTENT_LENGTH, Integer.toString(body.length));
        }
        this.header = newHeader;

    }

    public static HttpResponse ok(Map<String, String> header, byte[] body) {
        return new HttpResponse(OK_STATUS_LINE, header, body);
    }

    public static HttpResponse redirect(Map<String, String> header, String redirectLocation) {
        Map<String, String> headerWithRedirect = new HashMap<>(header);
        headerWithRedirect.put(LOCATION, redirectLocation);

        return new HttpResponse(REDIRECT_STATUS_ILNE, headerWithRedirect, "".getBytes());
    }


}
