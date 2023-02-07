package webserver;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public enum ResourceType {
    HTML("./templates", List.of("html", "ico")),
    STATIC("./static", List.of("css", "js", "png", "eot", "svg", "ttf", "woff", "woff2")),
    NONE("", Collections.emptyList());

    private final String path;
    private final List<String> extensions;

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
