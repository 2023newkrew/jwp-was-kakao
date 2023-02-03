package http.request;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestBody {
    private final RequestParam requestParam;

    public RequestBody(BufferedReader reader) {
        try {
            requestParam = new RequestParam(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RequestBody(String query) {
        requestParam = new RequestParam(query);
    }
}
