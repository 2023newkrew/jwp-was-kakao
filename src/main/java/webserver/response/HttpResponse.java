package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private final Map<HttpResponseHeader, String> headers = new HashMap<>();
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

    public void send() {
        if (httpVersion == null || status == null) {
            throw new RuntimeException("Response Status Line은 비어있을 수 없습니다.");
        }
        try {
            dos.writeBytes(httpVersion.getHttpVersion() + " " + status.getStatus() + " \r\n");
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

    public enum HttpResponseHeader {
        CONTENT_TYPE("Content-Type"),
        CONTENT_LENGTH("Content-Length"),
        LOCATION("Location"),
        ;

        private final String header;

        HttpResponseHeader(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }

    public enum HttpResponseStatus {
        OK("200 OK"),
        REDIRECT("302 Found");

        private final String status;

        HttpResponseStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public enum HttpVersion {
        HTTP1_1("HTTP/1.1");

        private final String httpVersion;

        HttpVersion(String httpVersion) {
            this.httpVersion = httpVersion;
        }

        public String getHttpVersion() {
            return httpVersion;
        }
    }
}
