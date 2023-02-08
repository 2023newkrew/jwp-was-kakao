package webserver.handler.resource.resolver.view;

import webserver.content.Content;
import webserver.content.ContentType;
import webserver.handler.resource.resolver.AbstractResolver;

public class ViewResolver extends AbstractResolver {

    private final ContentType contentType;

    public ViewResolver(String prefix, ContentType contentType) {
        super(prefix);
        this.contentType = contentType;
    }

    @Override
    public Content resolve(String path) {
        return new Content(contentType, getData(path));
    }
}
