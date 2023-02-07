package webserver.handler.resolver;

import webserver.http.content.Content;


public interface Resolver {

    boolean isResolvable(String path);

    Content resolve(String path);
}
