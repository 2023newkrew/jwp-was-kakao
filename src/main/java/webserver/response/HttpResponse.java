package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private final Map<HttpResponseHeader, String> headers = new HashMap<>();
    private final List<Cookie> cookies = new ArrayList<>();
    private final DataOutputStream dos;

    private HttpVersion httpVersion;
    private HttpResponseStatus status;

    private byte[] body;

    private HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public static HttpResponse of(DataOutputStream dos) {
        return new HttpResponse(dos);
    }

    public void setStatusLine(HttpResponseStatus status) {
        setStatusLine(status, HttpVersion.HTTP1_1);
    }

    public void setStatusLine(HttpResponseStatus status, HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
        this.status = status;
    }

    public void addHeader(HttpResponseHeader header, String value) {
        headers.put(header, value);
    }

    public void addBody(byte[] body) {
        this.body = body;
    }

    public void addCookie(Cookie... cookies) {
        this.cookies.addAll(List.of(cookies));
    }

    public void send() {
        if (httpVersion == null || status == null) {
            throw new RuntimeException("Response Status Line은 비어있을 수 없습니다.");
        }
        try {
            dos.writeBytes(httpVersion.getHttpVersion() + " " + status.getStatus() + " \r\n");
            for (Cookie cookie : cookies) {
                dos.writeBytes("Set-Cookie: " + cookie.getName() + "=" + cookie.getValue() + "\r\n");
            }
            for (final var entry : headers.entrySet()) {
                dos.writeBytes(entry.getKey().getHeader() + ": " + entry.getValue() + " \r\n");
            }
            dos.writeBytes("\r\n");
            if (body != null) {
                dos.write(body, 0, body.length);
            }
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
