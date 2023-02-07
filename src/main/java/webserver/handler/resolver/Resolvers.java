package webserver.handler.resolver;

import webserver.http.content.Content;

import java.util.List;

public class Resolvers {
    private final List<Resolver> resolvers;

    public Resolvers(Resolver... resolvers) {
        this(List.of(resolvers));
    }

    public Resolvers(List<Resolver> resolvers) {
        this.resolvers = resolvers;
    }

    public boolean isResolvable(String path) {
        return resolvers.stream()
                .filter(resolver -> resolver.isResolvable(path))
                .count() == 1L;
    }

    public Content resolve(String path) {
        return resolvers.stream()
                .filter(resolver -> resolver.isResolvable(path))
                .findAny()
                .orElseThrow(RuntimeException::new)
                .resolve(path);
    }
}
