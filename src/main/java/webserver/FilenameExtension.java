package webserver;

import java.util.Arrays;

public enum FilenameExtension {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    ICO("ico" ,"image/x-icon"),
    EOT("eot", "application/vnd.ms-fontobject"),
    SVG("svg", "image/svg+xml"),
    TTF("ttf", "application/x-font-ttf"),
    WOFF("woff", "application/x-font-woff"),
    WOFF2("woff2", "application/x-font-woff2"),
    PNG("png" ,"image/png"),
    DEFAULT("", "text/html");

    private final String extension;
    private final String contentType;

    FilenameExtension(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static FilenameExtension from(String extension) {
        return Arrays.stream(FilenameExtension.values())
                .filter((filenameExtension -> filenameExtension.extension.equals(extension)))
                .findAny()
                .orElse(DEFAULT);
    }

    public String getContentType() {
        return contentType;
    }

    private boolean isFont() {
        return this == FilenameExtension.EOT ||
                this == FilenameExtension.SVG ||
                this == FilenameExtension.TTF ||
                this == FilenameExtension.WOFF ||
                this == FilenameExtension.WOFF2;
    }

    public boolean isExistStaticFolder() {
        return this == FilenameExtension.CSS ||
                this == FilenameExtension.PNG ||
                this == FilenameExtension.JS ||
                this.isFont();
    }

    public boolean isExistTemplateFolder() {
        return this == FilenameExtension.HTML || this == FilenameExtension.ICO;
    }
}
