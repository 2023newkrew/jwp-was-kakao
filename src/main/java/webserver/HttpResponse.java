package webserver;

public class HttpResponse {

    private ResponseHeader responseHeader;
    private byte[] responseBody;

    public HttpResponse() {
        responseHeader = new ResponseHeader();
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
}
