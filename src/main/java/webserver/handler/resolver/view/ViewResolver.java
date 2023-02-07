package webserver.handler.resolver.view;

import webserver.content.Content;
import webserver.content.ContentType;
import webserver.handler.resolver.AbstractResolver;

public class ViewResolver extends AbstractResolver {

    private static final String PREFIX = "./templates";

    public ViewResolver() {
        super(PREFIX);
    }

    @Override
    public Content resolve(String path) {
        return new Content(ContentType.TEXT_HTML, getData(path));
    }
}
