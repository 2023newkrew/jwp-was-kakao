package webserver;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
public enum ResourceType {
    HTML("./templates", Set.of("html", "ico")),
    STATIC("./static", Set.of("css", "js", "png", "eot", "svg", "ttf", "woff", "woff2")),
    NONE("", Collections.emptySet());

    private final String path;
    private final Set<String> extensions;

    public String getPath() {
        return path;
    }

    public static ResourceType getResourceType(String extension) {
        return Arrays.stream(ResourceType.values())
                .filter(v -> v.extensions.contains(extension))
                .findFirst()
                .orElse(NONE);
    }
}
