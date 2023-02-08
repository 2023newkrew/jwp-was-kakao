package mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class FakeBufferedReader extends BufferedReader {
    final String[] httpRequest = {
            "GET /index.html HTTP/1.1 ",
            "Host: localhost:8080 ",
            "Connection: keep-alive ",
            "",
            ""
    };
    int idx = 0;

    public FakeBufferedReader() {
        super(new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }

            @Override
            public void close() throws IOException {

            }
        });
    }
    @Override
    public String readLine() throws IOException {
        return httpRequest[idx++];
    }
}
