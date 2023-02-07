package webserver;

import java.util.Arrays;

public enum FilenameExtension {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    ICO("ico" ,"image/vnd.microsoft.icon"),
    PNG("png" ,"image/png"),
    WOFF("woff", "application/x-font-woff"),
    WOFF2("woff2", "application/x-font-woff2"),
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
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getContentType() {
        return contentType;
    }
}
