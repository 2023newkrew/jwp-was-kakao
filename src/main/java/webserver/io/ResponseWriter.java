package webserver.io;

import webserver.http.content.Content;
import webserver.http.response.Response;
import webserver.http.response.ResponseHeader;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class ResponseWriter implements Closeable {
    private final DataOutputStream dos;

    public ResponseWriter(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void write(Response response) throws IOException {
        ResponseHeader header = response.getHeader();
        Content body = response.getBody();

        dos.write(header.getBytes());
        if (Objects.nonNull(body)) {
            dos.write(body.getBytes());
        }
    }

    @Override
    public void close() throws IOException {
        dos.close();
    }
}
