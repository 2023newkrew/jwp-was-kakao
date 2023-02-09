package webserver.handler.resource.resolver.statics;

import webserver.http.ContentType;

public class StaticType {

    private final String prefix;

    public final ContentType contentType;

    public StaticType(String prefix, ContentType contentType) {
        this.prefix = prefix;
        this.contentType = contentType;
    }

    public String getPrefix() {
        return prefix;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
