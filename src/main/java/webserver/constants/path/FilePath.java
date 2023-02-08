package webserver.constants.path;

import webserver.constants.extension.CssType;
import webserver.constants.extension.FontType;
import webserver.constants.extension.HtmlType;
import webserver.constants.extension.ImageType;
import webserver.constants.extension.JsType;

public enum FilePath {
    HTML("templates"),
    CSS("static"),
    JS("static"),
    FONT("static"),
    IMAGE("static");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static String getPathByExtension(String extension) {
        if (HtmlType.isHtmlByExtension(extension)) {
            return FilePath.valueOf("HTML").getPath();
        }

        if (CssType.isCssByExtension(extension)) {
            return FilePath.valueOf("CSS").getPath();
        }

        if (JsType.isJsByExtension(extension)) {
            return FilePath.valueOf("JS").getPath();
        }

        if (FontType.isFontByExtension(extension)) {
            return FilePath.valueOf("FONT").getPath();
        }

        if (ImageType.isImageByExtension(extension)) {
            return FilePath.valueOf("IMAGE").getPath();
        }

        return null;
    }
}
