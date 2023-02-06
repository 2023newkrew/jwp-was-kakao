package webserver;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public enum ResourceType {
    HTML("./templates", List.of("html")),
    STATIC("./static", List.of("css", "js")),
    NONE("", Collections.emptyList());

    private final String path;
    private final List<String> extensions;

    public String getPath() {
        return path;
    }

    public static ResourceType getResourceType(Request request) {
        String[] splitedUrl = request.getUrl().split("\\.");
        String requestExtension = splitedUrl[splitedUrl.length - 1];

        return Arrays.stream(ResourceType.values())
                .filter(v -> v.extensions.contains(requestExtension))
                .findFirst()
                .orElse(NONE);
    }
}
