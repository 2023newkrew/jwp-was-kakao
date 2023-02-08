package webserver.handler.resource.resolver;

import webserver.content.Content;

public interface Resolver {

    boolean isResolvable(String path);

    Content resolve(String path);
}
