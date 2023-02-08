package http.request;

public class RequestBody {
    private final RequestParam requestParam;

    public RequestBody(String query) {
        requestParam = new RequestParam(query);
    }

    public RequestParam getRequestParam() {
        return requestParam;
    }
}
