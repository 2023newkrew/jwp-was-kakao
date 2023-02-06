package webserver.resolver;

import webserver.content.Content;
import webserver.request.Path;

import java.util.List;

public class ContentResolvers {
    private final List<ContentResolver> resolvers;

    public ContentResolvers(List<ContentResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public boolean isResolvable(Path path) {
        return resolvers.stream()
                .filter(resolver -> resolver.isResolvable(path))
                .count() == 1L;
    }

    public Content resolve(Path path) {
        return resolvers.stream()
                .filter(resolver -> resolver.isResolvable(path))
                .findAny()
                .orElseThrow(RuntimeException::new)
                .resolve(path);
    }
}
