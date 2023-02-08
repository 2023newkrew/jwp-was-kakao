package webserver.resolver;

import webserver.content.Content;

public class ResourceResolver extends AbstractResolver {

    private static final String PREFIX = "./static";

    public ResourceResolver() {
        super(PREFIX);
    }

    @Override
    public Content resolve(String path) {
        if (!isResolvable(path)) {
            throw new RuntimeException();
        }
        var resourceType = ResourceType.from(path);

        return new Content(resourceType.getContentType(), getData(path));
    }
}
