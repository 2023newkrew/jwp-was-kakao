package utils.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponseVersion1 implements HttpResponse{
    private HttpResponseCode responseCode = null;
    private final Map<String, String> header = new LinkedHashMap<>();
    private byte[] body = new byte[]{};
    @Override
    public HttpResponse setResponseCode(int responseCode) {
        this.responseCode = HttpResponseCode.findByStatusCode(responseCode);
        return this;
    }

    @Override
    public HttpResponse setResponseCode(HttpResponseCode httpResponseCode) {
        this.responseCode = httpResponseCode;
        return this;
    }

    @Override
    public HttpResponse setHeader(String parameter, String value) {
        header.put(parameter, value);
        return this;
    }

    @Override
    public HttpResponse setBody(byte[] body) {
        header.put("Content-Length", Integer.toString(body.length));
        this.body = body;
        return this;
    }

    @Override
    public void respond(DataOutputStream dos) throws IOException {
        respondFirstLine(dos);
        respondHeader(dos);
        respondBody(dos);
    }

    private void respondFirstLine(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 ");
        if (Objects.isNull(responseCode)){
            throw new IllegalStateException("Response Code should be designated.");
        }
        dos.writeBytes(responseCode + " \r\n");
    }

    private void respondHeader(DataOutputStream dos) throws IOException{
        for (String param : header.keySet()){
            respondHeaderOneLine(param, header.get(param), dos);
        }
        dos.writeBytes("\r\n");
    }

    private void respondHeaderOneLine(String parameter, String value, DataOutputStream dos) throws IOException {
        dos.writeBytes(parameter + ": " + value + " \r\n");
    }

    private void respondBody(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
