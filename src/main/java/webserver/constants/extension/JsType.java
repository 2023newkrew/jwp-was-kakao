package webserver.constants.extension;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum JsType {
    JS("js");

    private final String extension;
    private static final Map<String, JsType> reverseMap = new HashMap<>();

    static {
        for (JsType jsType : EnumSet.allOf(JsType.class)) {
            reverseMap.put(jsType.getExtension(), jsType);
        }
    }

    JsType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static Boolean isJsByExtension(String extension) {
        if (reverseMap.containsKey(extension)) {
            return true;
        }

        return false;
    }
}
