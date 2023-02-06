package webserver.resolver;

import webserver.content.ContentType;
import webserver.request.Path;

import java.util.Arrays;

public enum ResourceType {
    CSS("/css", ContentType.TEXT_CSS),
    FONT("/fonts", ContentType.TEXT_CSS),
    IMAGE("/images", ContentType.TEXT_CSS),
    JAVA_SCRIPT("/js", ContentType.TEXT_CSS);

    private final String path;

    private final ContentType contentType;

    ResourceType(String path, ContentType contentType) {
        this.path = path;
        this.contentType = contentType;
    }


    public String getPath() {
        return path;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public static ResourceType from(Path path) {
        return Arrays.stream(values())
                .filter(type -> path.startWith(type.getPath()))
                .findAny()
                .orElse(null);
    }
}
