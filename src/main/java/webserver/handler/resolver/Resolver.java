package webserver.handler.resolver;

import webserver.content.Content;


public interface Resolver {

    boolean isResolvable(String path);

    Content resolve(String path);
}
