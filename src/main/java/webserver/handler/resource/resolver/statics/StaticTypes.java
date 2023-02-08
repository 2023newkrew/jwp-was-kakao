package webserver.handler.resource.resolver.statics;

import webserver.content.ContentType;

import java.util.List;

public class StaticTypes {

    private final List<StaticType> staticTypes;

    public StaticTypes(StaticType... staticTypes) {
        this(List.of(staticTypes));
    }

    public StaticTypes(List<StaticType> staticTypes) {
        this.staticTypes = staticTypes;
    }

    public ContentType getContentType(String path, ContentType defaultType) {
        return staticTypes.stream()
                .filter(type -> path.startsWith(type.getPrefix()))
                .map(StaticType::getContentType)
                .findAny()
                .orElse(defaultType);
    }
}
