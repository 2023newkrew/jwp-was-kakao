package webserver.resolver;

import webserver.content.Content;
import webserver.request.Path;

public interface ContentResolver {

    boolean isResolvable(Path path);

    Content resolve(Path path);
}
