package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

    public void send() throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        if (this.getResponseHeader() != null) {
            dos.writeBytes(this.getResponseHeader().getValue());
        }
        if (this.getResponseBody() != null) {
            dos.write(this.getResponseBody());
        }
        dos.flush();
    }
}
