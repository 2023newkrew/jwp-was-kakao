package webserver.handler.resolver.view;

import webserver.handler.resolver.AbstractResolver;
import webserver.http.content.Content;
import webserver.http.content.ContentType;

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
