package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class Request {
    private final String path;

    public Request(InputStream in) throws IOException {
        var br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        System.out.println(line);
        String[] headline = line.split(" ");
        path = headline[1];
        do {
            line = br.readLine();
            System.out.println(line);
        }
        while (Objects.nonNull(line) && !line.equals(""));
    }

    private String parsePath(String line) {
        return line.split(" ")[1];
    }

    public String getPath() {
        return path;
    }
}
