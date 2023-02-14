package webserver;

import exception.ErrorCode;
import exception.WasException;
import type.HttpStatusCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private final OutputStream outputStream;
    private ResponseHeader responseHeader;
    private byte[] responseBody;

    public HttpResponse(OutputStream outputStream) {
        responseHeader = new ResponseHeader();
        this.outputStream = outputStream;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setResponseHeader(ResponseHeader requestHeader) {
        this.responseHeader = requestHeader;
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public void sendRedirect(String url) {
        this.responseHeader.setHttpStatusCode(HttpStatusCode.REDIRECT);
        this.responseHeader.setLocation(url);
    }

    public void sendError(HttpStatusCode httpStatusCode, String message) {
        this.responseHeader.setHttpStatusCode(httpStatusCode);
        setResponseBody(message.getBytes(StandardCharsets.UTF_8));
        send();
    }

    public void send() {
        DataOutputStream dos = new DataOutputStream(outputStream);
        try {
            if (this.getResponseHeader() != null) {
                dos.writeBytes(this.getResponseHeader().getValue());
            }
            if (this.getResponseBody() != null) {
                dos.write(this.getResponseBody());
            }
            dos.flush();
        } catch (IOException e) {
            throw new WasException(ErrorCode.CAN_NOT_WRITE_DATA);
        }
    }
}
