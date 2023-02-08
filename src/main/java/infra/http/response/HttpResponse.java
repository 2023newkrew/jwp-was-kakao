package infra.http.response;

import infra.http.body.Body;
import infra.http.header.Headers;
import infra.http.HttpMessageBase;

public class HttpResponse extends HttpMessageBase {
    private StatusLine statusLine;

    public HttpResponse(ResponseStatus status, Body body) {
        super(new Headers(), body);
        this.statusLine = new StatusLine(status);
    }

    public HttpResponse(ResponseStatus status) {
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
        if (this.getBody() == null) {
            return upper;
        }

        byte[] body = this.getBody().flat();
        byte[] result = new byte[upper.length + body.length];
        System.arraycopy(upper, 0, result, 0, upper.length);
        System.arraycopy(body, 0, result, upper.length, body.length);
        return result;
    }
}
