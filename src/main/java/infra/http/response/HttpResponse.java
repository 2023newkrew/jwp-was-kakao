package infra.http.response;

import infra.http.Headers;
import infra.http.HttpMessageBase;

public class HttpResponse extends HttpMessageBase {
    private StatusLine statusLine;
    private byte[] body;

    public HttpResponse(HttpResponseStatus status, byte[] body) {
        super(new Headers());
        this.statusLine = new StatusLine(status);
        this.body = body;
    }

    public HttpResponse(HttpResponseStatus status) {
        this(status, null);
    }

    public byte[] flat() {
        StringBuilder sb = new StringBuilder(statusLine.toString());
        sb.append(HttpMessageBase.LINE_DELIMITER);
        Headers headers = super.getHeaders();
        if (headers != null) {
            sb.append(headers);
        }
        sb.append(HttpMessageBase.BODY_DELIMITER);

        byte[] upper = sb.toString().getBytes();
        if (body == null) {
            return upper;
        }

        byte[] result = new byte[upper.length + body.length];
        System.arraycopy(upper, 0, result, 0, upper.length);
        System.arraycopy(body, 0, result, upper.length, body.length);
        return result;
    }
}
