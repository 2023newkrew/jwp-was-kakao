package utils.response;

import java.io.DataOutputStream;
import java.io.IOException;

public interface HttpResponse {
    HttpResponse setResponseCode(int responseCode);
    HttpResponse setResponseCode(HttpResponseCode httpResponseCode);
    HttpResponse setHeader(String parameter, String value);
    HttpResponse setBody(byte[] body);
    void respond(DataOutputStream dos) throws IOException;
}
