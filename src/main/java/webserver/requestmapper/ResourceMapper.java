package webserver.requestmapper;

import type.ContentType;
import utils.FileIoUtils;
import webserver.parser.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResourceMapper {

    public static final ResourceMapper INSTANCE = new ResourceMapper();

    private static final List<String> roots = List.of("templates", "static");

    private ResourceMapper() {
    }

    public Response handle(String uri) throws IOException, URISyntaxException {
        byte[] returnBody;
        for (String root : roots) {
            returnBody = FileIoUtils.loadFileFromClasspath(root + uri);
            if (returnBody != null) {
                String[] split = uri.split("\\.");
                String ext = split[split.length - 1];
                return Response.ok()
                        .contentType(ContentType.valueOf(ext.toUpperCase()))
                        .body(new String(returnBody, StandardCharsets.UTF_8))
                        .build();
            }
        }
        return Response.notFound().build();
    }
}
