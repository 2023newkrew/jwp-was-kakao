package webserver;

import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class GetRequestHandler {
    public static byte[] handle(HttpRequest httpRequest) throws IOException, URISyntaxException {
        String uri = httpRequest.getUri();

        if (uri.equals("/")) {
            return "Hello world".getBytes();
        }

        String path = "";

        if(uri.endsWith(".html")){
            path = "templates";
        }
        if(uri.endsWith(".css") || uri.endsWith(".js") || uri.startsWith("/fonts") || uri.startsWith("/images")){
            path = "static";
        }

        return FileIoUtils.loadFileFromClasspath(path + uri);
    }
}
