package webserver.handler.resolver;

import webserver.response.ResponseBody;

public interface Resolver {

    boolean isResolvable(String path);

    ResponseBody resolve(String path);
}
