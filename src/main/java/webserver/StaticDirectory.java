package webserver;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum StaticDirectory {
    CSS, FONTS, IMAGES, JS;

    private static final Map<String, StaticDirectory> mappings = new HashMap<>(16);

    static {
        for (StaticDirectory directory : values()) {
            mappings.put(directory.name(), directory);
        }
    }

    @Nullable
    public static StaticDirectory resolve(@Nullable String method) {
        return (method != null ? mappings.get(method) : null);
    }

}
