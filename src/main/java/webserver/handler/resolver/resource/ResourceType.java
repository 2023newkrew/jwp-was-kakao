package webserver.handler.resolver.resource;

import webserver.http.content.ContentType;

import java.util.Arrays;

public enum ResourceType {
    CSS("/css", ContentType.TEXT_CSS),
    FONT("/fonts", ContentType.FONT_TTF),
    IMAGE("/images", ContentType.IMAGE_PNG),
    JAVA_SCRIPT("/js", ContentType.TEXT_JAVASCRIPT);

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

    public static ResourceType from(String uri) {
        return Arrays.stream(values())
                .filter(type -> uri.startsWith(type.getPath()))
                .findAny()
                .orElse(null);
    }
}
