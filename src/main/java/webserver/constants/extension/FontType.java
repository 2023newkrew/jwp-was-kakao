package webserver.constants.extension;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum FontType {
    TFF("tff"),
    OTF("otf"),
    EOT("eot"),
    SVG("svg"),
    SVGZ("svgz"),
    WOFF("woff"),
    WOFF2("woff2");

    private final String extension;
    private static final Map<String, FontType> reverseMap = new HashMap<>();

    static {
        for (FontType fontType : EnumSet.allOf(FontType.class)) {
            reverseMap.put(fontType.getExtension(), fontType);
        }
    }

    FontType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static Boolean isFontByExtension(String extension) {
        if (reverseMap.containsKey(extension)) {
            return true;
        }

        return false;
    }
}
