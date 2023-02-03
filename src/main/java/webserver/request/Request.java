package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class Request {
    private final String path;

    public Request(InputStream in) throws IOException {
        try (
                var isr = new InputStreamReader(in);
                var br = new BufferedReader(isr)
        ) {
            String line = br.readLine();
            path = parsePath(line);
            while (Objects.nonNull(line) && !line.equals("")) {
                line = br.readLine();
            }
        }
    }

    private String parsePath(String line) {
        return line.split(" ")[1];
    }

    public String getPath() {
        return path;
    }
}
