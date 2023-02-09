package webserver.handler.resource.resolver;

import webserver.http.Content;

public interface Resolver {

    boolean isResolvable(String path);

    Content resolve(String path);
}
