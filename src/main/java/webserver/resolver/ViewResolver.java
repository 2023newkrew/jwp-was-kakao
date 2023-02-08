package webserver.resolver;

import webserver.content.Content;
import webserver.content.ContentType;

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
