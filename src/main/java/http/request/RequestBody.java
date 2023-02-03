package http.request;

public class RequestBody {
    private final String body;

    public RequestBody(String body) {
        this.body = body;
    }

    public RequestParam getRequestParam() {
        return new RequestParam(body);
    }
}
