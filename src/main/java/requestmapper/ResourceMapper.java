package requestmapper;

import response.Response;
import response.ContentType;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResourceMapper {

    private static final List<String> roots = List.of("templates", "static");

    private static class LazyHolder {
        static final ResourceMapper INSTANCE = new ResourceMapper();
    }

    private ResourceMapper() {
    }

    public static ResourceMapper getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Response handle(String uri) throws IOException, URISyntaxException {
        byte[] returnBody;
        for (String root : roots) {
            returnBody = FileIoUtils.loadFileFromClasspath(root + uri);
            if (returnBody != null) {
                String[] split = uri.split("\\.");
                String extension = split[split.length - 1];
                return Response.ok()
                        .contentType(ContentType.valueOf(extension.toUpperCase()))
                        .body(new String(returnBody, StandardCharsets.UTF_8))
                        .build();
            }
        }
        return Response.notFound().build();
    }
}
