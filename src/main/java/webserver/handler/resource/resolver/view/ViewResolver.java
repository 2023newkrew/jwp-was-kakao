package webserver.handler.resource.resolver.view;

import webserver.handler.resource.resolver.AbstractResolver;
import webserver.http.Content;
import webserver.http.ContentType;

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
