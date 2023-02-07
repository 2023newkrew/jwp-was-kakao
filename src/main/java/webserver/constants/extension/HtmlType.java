package webserver.constants.extension;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum HtmlType {
    HTML("html");

    private final String extension;
    private static final Map<String, HtmlType> reverseMap = new HashMap<>();

    static {
        for (HtmlType htmlType : EnumSet.allOf(HtmlType.class)) {
            reverseMap.put(htmlType.getExtension(), htmlType);
        }
    }

    HtmlType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static Boolean isHtmlByExtension(String extension) {
        if (reverseMap.containsKey(extension)) {
            return true;
        }

        return false;
    }
}
