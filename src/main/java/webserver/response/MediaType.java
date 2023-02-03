package webserver.response;

public enum MediaType {
    TEXT_HTML("text/html;charset=utf-8"),
    TEXT_CSS("text/css;charset=utf-8");

    final String header;

    MediaType(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
