package webserver.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {

    private final Path path;

    public Request(List<String> request) {
        String line = URLDecoder.decode(request.get(0), StandardCharsets.UTF_8);
        String[] headline = line.split(" ");
        path = new Path(headline[1]);
    }

    public Path getPath() {
        return path;
    }
}
