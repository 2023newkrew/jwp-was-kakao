package webserver.constants.extension;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CssType {
    CSS("css");

    private final String extension;
    private static final Map<String, CssType> reverseMap = new HashMap<>();

    static {
        for (CssType cssType : EnumSet.allOf(CssType.class)) {
            reverseMap.put(cssType.getExtension(), cssType);
        }
    }

    CssType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static Boolean isCssByExtension(String extension) {
        if (reverseMap.containsKey(extension)) {
            return true;
        }

        return false;
    }
}
