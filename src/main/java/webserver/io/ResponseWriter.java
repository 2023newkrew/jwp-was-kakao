package webserver.io;

import webserver.response.Response;

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
        ResponseHeader header = new ResponseHeader(response);
        dos.write(header.getBytes());
        if (Objects.nonNull(response.getBody())) {
            dos.write(response.getBytes());
        }
    }

    @Override
    public void close() throws IOException {
        dos.close();
    }
}
