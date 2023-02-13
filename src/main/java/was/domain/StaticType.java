package was.domain;

public enum StaticType {
    HTML("html", "text/html;charset=utf-8", "templates"),
    CSS("css", "text/css", "static"),
    PNG("png", "image/png", "static"),
    JS("js", "text/javascript", "static"),
    TTF("ttf", "font/ttf", "static"),
    WOFF("woff", "font/woff", "static"),
    ICO("ico", "image/vnd.microsoft.icon", "templates");

    private String fileType;
    private String contentType;
    private String basePath;

    StaticType(String fileType, String contentType, String basePath) {
        this.fileType = fileType;
        this.contentType = contentType;
        this.basePath = basePath;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileType() {
        return fileType;
    }

    public String getBasePath() {
        return basePath;
    }
}
