package webserver.request.support;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum StaticDirectory {
    CSS, FONTS, IMAGES, JS;

    private static final int DIR_SIZE = 4;
    private static final Map<String, StaticDirectory> mappings = new HashMap<>(DIR_SIZE);

    static {
        for (StaticDirectory directory : values()) {
            mappings.put(directory.name(), directory);
        }
    }

    @Nullable
    public static StaticDirectory resolve(@Nullable String dir) {
        if (dir != null) {
            return mappings.get(dir);
        }
        return null;
    }

}
