package webserver.response;

public enum HttpResponseContentType {
    HTML(".html", "text/html; charset=utf-8"),
    CSS(".css", "text/css; charset=utf-8"),
    JS(".js", "text/javascript; charset=utf-8"),
    WOFF(".woff", "application/x-font-woff"),
    TTF(".ttf", "application/x-font-ttf"),
    ICO(".ico", "image/x-icon");

    private final String suffix;
    private final String contentType;

    HttpResponseContentType(String suffix, String contentType) {
        this.suffix = suffix;
        this.contentType = contentType;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getContentType() {
        return contentType;
    }
}
