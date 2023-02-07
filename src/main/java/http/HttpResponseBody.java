package http;

class HttpResponseBody {
    private final byte[] body;

    public HttpResponseBody(byte[] body) {
        this.body = body;
    }

    public static HttpResponseBody from(String body) {
        return new HttpResponseBody(body.getBytes());
    }

    public byte[] getBytes() {
        return body;
    }
}
